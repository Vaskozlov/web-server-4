package org.vaskozov.lab4.servlet;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.lab4.bean.CheckResult;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.InAreaCheckerInterface;
import org.vaskozov.lab4.service.ResultsStorageInterface;

@ApplicationScoped
@Path("user/check")
@PermitAll
public class AreaCheckServlet {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig().withFormatting(true);
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @EJB(name = "java:global/lab4/ResultsStorage")
    private ResultsStorageInterface resultsStorage;

    @EJB(name = "java:global/lab4/InAreaChecker")
    private InAreaCheckerInterface inAreaChecker;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response check(
            @QueryParam("x") double x,
            @QueryParam("y") double y,
            @QueryParam("r") double r,
            @Context HttpServletRequest request
    ) {
        long executionBeginNs = System.nanoTime();
        Result<Void, String> validatedParameters = validateParameters(x, y, r);

        if (validatedParameters.isError()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(validatedParameters.getError())
                    .build();
        }

        String login = (String) request.getAttribute("login");
        boolean isInArea = inAreaChecker.check(x, y, r);

        CheckResult checkResult = new CheckResult(
                x,
                y,
                r,
                isInArea,
                System.nanoTime() - executionBeginNs
        );

        if (!resultsStorage.save(login, checkResult)) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response
                .ok(JSONB.toJson(checkResult))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .build();
    }

    private Result<Void, String> validateParameters(double x, double y, double r) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(r)) {
            return Result.error("Parameters must be numbers");
        }

        if (Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(r)) {
            return Result.error("Parameters must be finite numbers");
        }

        if (r <= 0.0) {
            return Result.error("R must be greater than 0");
        }

        return Result.success(null);
    }
}
