package org.vaskozov.lab4.servlet;

import jakarta.ejb.EJB;
import jakarta.persistence.Transient;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.lib.AuthorizationInfoError;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.AuthorizationInterface;

import java.io.IOException;

@WebServlet(urlPatterns = {"/auth", "/register"})
public class AuthorizationServlet extends HttpServlet {
    @EJB(name = "java:global/lab4/AuthorizationService")
    private AuthorizationInterface authorizationService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Result<Login, AuthorizationInfoError> login = Login.of(request.getParameter("login"));
        Result<Password, AuthorizationInfoError> password = Password.of(request.getParameter("password"));

        if (login.isError()) {
            response.getWriter().println(login.getError());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (password.isError()) {
            response.getWriter().println(password.getError());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setHeader("Content-Type", "text/plain");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String path = request.getServletPath();

        if (path.equals("/auth")) {
            authorize(response, login.getValue(), password.getValue());
        } else if (path.equals("/register")) {
            register(response, login.getValue(), password.getValue());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void authorize(HttpServletResponse response, Login login, Password password) {
        if (authorizationService.authorize(login, password)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void register(HttpServletResponse response, Login login, Password password) {
        if (authorizationService.register(login, password)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}