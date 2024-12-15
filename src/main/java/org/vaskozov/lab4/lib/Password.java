package org.vaskozov.lab4.lib;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Password {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String SALT = "1492@@#a!";

    private final String passwordRepresentation;

    @Override
    public String toString() {
        return passwordRepresentation;
    }

    public String getHash() {
        return Hashing.sha384()
                .hashString(
                        SALT + passwordRepresentation,
                        java.nio.charset.StandardCharsets.UTF_8
                )
                .toString();
    }

    public static Result<Password, AuthorizationInfoError> of(String password) {
        if (password.length() < 8) {
            return Result.error(AuthorizationInfoError.TOO_SHORT);
        }

        if (!password.matches(PASSWORD_REGEX)) {
            return Result.error(AuthorizationInfoError.INVALID_CHARACTER);
        }

        return Result.success(new Password(password));
    }

    public static Password of(String password, boolean ignoreValidation) {
        if (ignoreValidation) {
            return new Password(password);
        }

        return of(password).getValue();
    }
}
