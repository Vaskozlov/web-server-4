package org.vaskozov.lab4.lib;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Login {
    private static final String LOGIN_REGEX = "^[a-zA-Z0-9]+$";

    private final String loginRepresentation;

    @Override
    public String toString() {
        return loginRepresentation;
    }

    public static Result<Login, AuthorizationInfoError> of(String login) {
        if (login.length() < 4) {
            return Result.error(AuthorizationInfoError.TOO_SHORT);
        }

        if (!login.matches(LOGIN_REGEX)) {
            return Result.error(AuthorizationInfoError.INVALID_CHARACTER);
        }

        return Result.success(new Login(login));
    }

    public static Login of(String login, boolean ignoreValidation) {
        if (ignoreValidation) {
            return new Login(login);
        }

        return of(login).getValue();
    }
}
