package org.vaskozov.lab4.service;

import org.vaskozov.lab4.bean.User;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;
import org.vaskozov.lab4.lib.Result;

public interface AuthorizationInterface {
    Result<User, String> authorize(Login login, Password password);

    Result<User, String> register(Login login, Password password, String role);

    String createToken(User user);

    Result<String, Exception> validateToken(String token);
}
