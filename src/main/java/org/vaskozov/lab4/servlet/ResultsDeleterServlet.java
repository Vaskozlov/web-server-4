package org.vaskozov.lab4.servlet;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import org.vaskozov.lab4.service.ResultsStorageInterface;

@ApplicationScoped
@Path("user/delete_results")
@PermitAll
public class ResultsDeleterServlet {
    @EJB(name = "java:global/lab4/ResultsStorage")
    private ResultsStorageInterface resultsStorage;

    @DELETE
    public Response deleteResults(@Context HttpServletRequest request) {
        String login = (String) request.getAttribute("login");
        resultsStorage.removeAll(login);

        return Response
                .ok("All results for user %s have been removed".formatted(login))
                .build();
    }
}
