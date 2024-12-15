package org.vaskozov.lab4.filters;

import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.lib.JwtError;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.UserAuthorizationInterface;

import java.io.IOException;

@Priority(2)
@WebFilter(urlPatterns = {"/api/user/*"}, asyncSupported = true)
public class AuthorizationFilter implements Filter {
    @EJB(name = "java:global/lab4/UserAuthorizer")
    private UserAuthorizationInterface userAuthorizer;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.getWriter().println("Authorization header is missing or invalid");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        Result<String, JwtError> validationResult = userAuthorizer.validateToken(token);

        if (validationResult.isError()) {
            JwtError error = validationResult.getError();
            httpResponse.getWriter().println(error.getMessage());
            httpResponse.setStatus(error.getStatus());
            CorsFilter.setCorsHeaders(httpResponse);
            return;
        }

        request.setAttribute("login", validationResult.getValue());
        chain.doFilter(request, response);
    }
}
