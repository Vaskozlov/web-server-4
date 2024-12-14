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
import org.vaskozov.lab4.bean.UserData;
import org.vaskozov.lab4.lib.*;
import org.vaskozov.lab4.service.AuthorizationInterface;

@ApplicationScoped
@Path("auth")
public class AuthorizationServlet {
    @EJB(name = "java:global/lab4/AuthorizationService")
    private AuthorizationInterface authorizationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("login")
    public Response login(@QueryParam("login") String qLogin, @QueryParam("password") String qPassword) {
        Result<AuthorizationInfo, Response> validationResult = validateLoginAndPassword(qLogin, qPassword);

        if (validationResult.isError()) {
            return validationResult.getError();
        }

        AuthorizationInfo authorizationInfo = validationResult.getValue();
        Login login = authorizationInfo.getLogin();
        Password password = authorizationInfo.getPassword();

        Result<UserData, String> loginResult = authorizationService.authorize(
                login,
                password
        );

        if (loginResult.isError()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(loginResult.getError())
                    .build();
        }

        UserData user = loginResult.getValue();
        return createResponseWithToken(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("register")
    public Response authenticate(@QueryParam("login") String qLogin, @QueryParam("password") String qPassword) {
        Result<AuthorizationInfo, Response> validationResult = validateLoginAndPassword(qLogin, qPassword);

        if (validationResult.isError()) {
            return validationResult.getError();
        }

        AuthorizationInfo authorizationInfo = validationResult.getValue();
        Login login = authorizationInfo.getLogin();
        Password password = authorizationInfo.getPassword();

        Result<UserData, String> authorizationResult = authorizationService.register(login, password, "user");

        if (authorizationResult.isError()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(authorizationResult.getError())
                    .build();
        }

        UserData user = authorizationResult.getValue();
        return createResponseWithToken(user);
    }

    private Result<AuthorizationInfo, Response> validateLoginAndPassword(String qLogin, String qPassword) {
        if (qLogin == null || qPassword == null) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Login and password must be provided")
                            .build()
            );
        }

        Result<Login, AuthorizationInfoError> login = Login.of(qLogin);
        Result<Password, AuthorizationInfoError> password = Password.of(qPassword);

        if (login.isError()) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(login.getError())
                            .build()
            );
        }

        if (password.isError()) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(password.getError())
                            .build()
            );
        }

        return Result.success(new AuthorizationInfo(login.getValue(), password.getValue()));
    }

    private Response createResponseWithToken(UserData user) {
        String token = authorizationService.createToken(user);

        JsonObject json = Json.createObjectBuilder()
                .add("token_type", "bearer")
                .add("role", user.getRole())
                .build();

        return Response.ok(json)
                .header("Cache-Control", "no-store")
                .header("Pragma", "no-cache")
                .header("Authorization", "Bearer " + token)
                .build();
    }
}
