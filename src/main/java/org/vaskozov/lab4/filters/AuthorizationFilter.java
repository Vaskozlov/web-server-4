package org.vaskozov.lab4.filters;

import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.AuthorizationInterface;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/user/*"}, asyncSupported = true)
public class AuthorizationFilter implements Filter {
    @EJB(name = "java:global/lab4/AuthorizationService")
    private AuthorizationInterface authorizationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            unauthorizedError(httpResponse, "Authorization header is missing or invalid");
            return;
        }

        String token = authHeader.substring(7);
        Result<String, Exception> validationResult = authorizationService.validateToken(token);

        if (validationResult.isError()) {
            unauthorizedError(httpResponse, validationResult.getError().getMessage());
            return;
        }

        request.setAttribute("login", validationResult.getValue());
        System.out.println("login: " + validationResult.getValue());
        chain.doFilter(request, response);
    }

    private void unauthorizedError(HttpServletResponse response, String reason) throws IOException {
        response.getWriter().println(reason);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
