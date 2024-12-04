package org.vaskozov.lab4.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.service.PointsValidationStorageInterface;

import java.io.IOException;

@WebServlet(urlPatterns = {"/validations_results"}, asyncSupported = true)
public class ValidationResultServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @EJB(name = "java:global/lab4/PointsValidationStorage")
    private transient PointsValidationStorageInterface validationStorage;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var login = request.getParameter("login");
        final var results = validationStorage.getAllValidations(login);

        if (results == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/json");
        gson.toJson(results, response.getWriter());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final var login = request.getParameter("login");
        validationStorage.removeAll(login);

        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().write("All results for user " + login + " have been removed");
    }
}
