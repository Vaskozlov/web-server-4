package org.vaskozov.lab4.lib;

import com.google.common.hash.Hashing;

public class Password {
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final String password;

    private Password(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return password;
    }

    public String getHash() {
        return Hashing.sha384().hashString(password, java.nio.charset.StandardCharsets.UTF_8).toString();
    }

    public static Result<Password, AuthorizationInfoError> of(String login) {
        if (login.length() < 8) {
            return Result.error(AuthorizationInfoError.TOO_SHORT);
        }

        if (!login.matches(passwordRegex)) {
            return Result.error(AuthorizationInfoError.INVALID_CHARACTER);
        }

        return Result.success(new Password(login));
    }
}
