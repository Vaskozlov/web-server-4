package org.vaskozov.lab4.lib;

public class Login {
    private static final String loginRegex = "^[a-zA-Z0-9]+$";

    private final String login;

    private Login(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return login;
    }

    public static Result<Login, AuthorizationInfoError> of(String login) {
        if (login.length() < 4) {
            return Result.error(AuthorizationInfoError.TOO_SHORT);
        }

        if (!login.matches(loginRegex)) {
            return Result.error(AuthorizationInfoError.INVALID_CHARACTER);
        }

        return Result.success(new Login(login));
    }
}
