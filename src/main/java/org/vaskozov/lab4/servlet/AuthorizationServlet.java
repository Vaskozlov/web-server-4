package org.vaskozov.lab4.servlet;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.lab4.bean.User;
import org.vaskozov.lab4.lib.*;
import org.vaskozov.lab4.service.UserAuthorizationInterface;

@ApplicationScoped
@Path("auth")
@PermitAll
public class AuthorizationServlet {
    @EJB(name = "java:global/lab4/UserAuthorizer")
    private UserAuthorizationInterface userAuthorizer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(@QueryParam("login") String qLogin, @QueryParam("password") String qPassword) {
        Result<User, String> loginResult = userAuthorizer.authorize(
                Login.of(qLogin, true),
                Password.of(qPassword, true)
        );

        if (loginResult.isError()) {
            return createErrorResponse(loginResult.getError(), Response.Status.UNAUTHORIZED);
        }

        return createTokenResponse(loginResult.getValue());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(@QueryParam("login") String qLogin, @QueryParam("password") String qPassword) {
        Result<AuthorizationInfo, Response> validationResult = validateLoginAndPassword(qLogin, qPassword);

        if (validationResult.isError()) {
            return validationResult.getError();
        }

        AuthorizationInfo authorizationInfo = validationResult.getValue();

        Result<User, String> authorizationResult = userAuthorizer.register(
                authorizationInfo.getLogin(),
                authorizationInfo.getPassword(),
                "user"
        );

        if (authorizationResult.isError()) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(authorizationResult.getError())
                    .build();
        }

        return createTokenResponse(authorizationResult.getValue());
    }

    private static Response createErrorResponse(String message, Response.Status status) {
        return Response
                .status(status)
                .entity(message)
                .build();
    }

    private Result<AuthorizationInfo, Response> validateLoginAndPassword(String qLogin, String qPassword) {
        if (qLogin == null || qPassword == null) {
            return Result.error(createErrorResponse("Login and password must be provided", Response.Status.BAD_REQUEST));
        }

        Result<Login, AuthorizationInfoError> login = Login.of(qLogin);
        Result<Password, AuthorizationInfoError> password = Password.of(qPassword);

        if (login.isError()) {
            return Result.error(createInvalidLoginResponse(login.getError()));
        }

        if (password.isError()) {
            return Result.error(createInvalidPasswordResponse(password.getError()));
        }

        return Result.success(new AuthorizationInfo(login.getValue(), password.getValue()));
    }

    private Response createTokenResponse(User user) {
        String token = userAuthorizer.createToken(user);

        JsonObject json = Json
                .createObjectBuilder()
                .add("token_type", "bearer")
                .add("role", user.getRole())
                .build();

        return Response
                .ok(json)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .header("Authorization", "Bearer " + token)
                .header("Access-Control-Expose-Headers", "Authorization")
                .build();
    }

    private static Response createInvalidLoginResponse(AuthorizationInfoError error) {
        switch (error) {
            case TOO_SHORT -> {
                return createErrorResponse("Login is too short", Response.Status.BAD_REQUEST);
            }
            case INVALID_CHARACTER -> {
                return createErrorResponse("Login contains invalid characters", Response.Status.BAD_REQUEST);
            }
        }

        throw new IllegalStateException();
    }

    private static Response createInvalidPasswordResponse(AuthorizationInfoError error) {
        switch (error) {
            case TOO_SHORT -> {
                return createErrorResponse("Password is too short", Response.Status.BAD_REQUEST);
            }
            case INVALID_CHARACTER -> {
                return createErrorResponse("Password contains invalid characters", Response.Status.BAD_REQUEST);
            }
        }

        throw new IllegalStateException();
    }
}
