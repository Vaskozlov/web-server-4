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
import org.vaskozov.lab4.bean.RequestResults;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.InAreaCheckerInterface;
import org.vaskozov.lab4.service.PointsValidationStorageInterface;

@ApplicationScoped
@Path("check")
public class AreaCheckServlet {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig().withFormatting(true);
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @EJB(name = "java:global/lab4/PointsValidationStorage")
    private PointsValidationStorageInterface validationStorage;

    @EJB(name = "java:global/lab4/InAreaChecker")
    private InAreaCheckerInterface inAreaChecker;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response check(
            @QueryParam("x") double x,
            @QueryParam("y") double y,
            @QueryParam("r") double r,
            @Context HttpServletRequest request
    ) {
        Result<Void, String> validationResult = validateParameters(x, y, r);

        if (validationResult.isError()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(validationResult.getError())
                    .build();
        }


        String login = (String) request.getAttribute("login");
        long executionBeginNs = System.nanoTime();
        boolean isInArea = inAreaChecker.check(x, y, r);

        RequestResults requestResult = new RequestResults(
                x,
                y,
                r,
                isInArea,
                System.nanoTime() - executionBeginNs
        );


        if (!validationStorage.save(login, requestResult)) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return Response
                .ok(JSONB.toJson(requestResult))
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .build();
    }

    private Result<Void, String> validateParameters(double x, double y, double r) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(r)) {
            return Result.error("Parameters must be numbers");
        }

        if (r <= 0) {
            return Result.error("R must be greater than 0");
        }

        return Result.success(null);
    }
}
