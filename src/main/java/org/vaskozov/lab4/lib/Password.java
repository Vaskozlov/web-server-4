package org.vaskozov.lab4.lib;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Password {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private final String password;

    @Override
    public String toString() {
        return password;
    }

    public String getHash() {
        return Hashing.sha384()
                .hashString(
                        "1492@@#a!" + password,
                        java.nio.charset.StandardCharsets.UTF_8
                )
                .toString();
    }

    public static Result<Password, AuthorizationInfoError> of(String login) {
        if (login.length() < 8) {
            return Result.error(AuthorizationInfoError.TOO_SHORT);
        }

        if (!login.matches(PASSWORD_REGEX)) {
            return Result.error(AuthorizationInfoError.INVALID_CHARACTER);
        }

        return Result.success(new Password(login));
    }
}
