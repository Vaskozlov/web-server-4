package org.vaskozov.lab4.filters;

import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;
import org.vaskozov.lab4.service.AuthorizationInterface;

import java.io.IOException;

@WebFilter(urlPatterns = {"/check", "/validations_results"}, asyncSupported = true)
public class AuthorizationFilter implements Filter {
    @EJB(name = "java:global/lab4/AuthorizationService")
    private AuthorizationInterface authorizationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String loginStr = request.getParameter("login");
        final String passwordStr = request.getParameter("password");

        if (loginStr == null || passwordStr == null) {
            unauthorizedError(httpResponse);
            return;
        }

        final var login = Login.of(loginStr);
        final var password = Password.of(passwordStr);

        if (login.isError() || password.isError()) {
            unauthorizedError(httpResponse);
            return;
        }

        if (!authorizationService.authorize(login.getValue(), password.getValue())) {
            response.getWriter().println("Invalid login or password");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }

    private void unauthorizedError(HttpServletResponse response) throws IOException {
        response.getWriter().println("Unauthorized");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
