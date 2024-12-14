package org.vaskozov.lab4.servlet;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.vaskozov.lab4.service.PointsValidationStorageInterface;

@ApplicationScoped
@Path("delete_results")
public class ResultsDeleterServlet {
    @EJB(name = "java:global/lab4/PointsValidationStorage")
    private PointsValidationStorageInterface validationStorage;

    @DELETE
    public Response deleteResults(@Context HttpServletRequest request) {
        String login = (String) request.getAttribute("login");
        validationStorage.removeAll(login);

        return Response
                .ok("All results for user %s have been removed".formatted(login))
                .build();
    }
}
