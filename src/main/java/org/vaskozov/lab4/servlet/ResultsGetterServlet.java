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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.lab4.bean.CheckResult;
import org.vaskozov.lab4.service.ResultsStorageInterface;

import java.util.List;

@ApplicationScoped
@Path("user/get_results")
@PermitAll
public class ResultsGetterServlet {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig().withFormatting(true);
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @EJB(name = "java:global/lab4/ResultsStorage")
    private ResultsStorageInterface resultsStorage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResults(@Context HttpServletRequest request) {
        String login = (String) request.getAttribute("login");

        List<CheckResult> results = resultsStorage.getAllResults(login);

        if (results == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        return Response
                .ok(JSONB.toJson(results))
                .build();
    }
}
