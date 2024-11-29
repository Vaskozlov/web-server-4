package org.vaskozov.lab4.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.service.PointsValidationStorageInterface;

@WebServlet(urlPatterns = {"/get_validations_results"}, asyncSupported = true)
public class GetValidationsResultsServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @EJB(name = "java:global/lab4/PointsValidationStorage")
    private transient PointsValidationStorageInterface validationStorage;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        final var login = request.getParameter("login");
        final var results = validationStorage.getAllValidations(login);

        if (results == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);

        try {
            gson.toJson(results, response.getWriter());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
