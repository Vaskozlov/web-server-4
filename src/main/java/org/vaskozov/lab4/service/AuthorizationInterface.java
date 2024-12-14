package org.vaskozov.lab4.service;

import org.vaskozov.lab4.bean.UserData;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;
import org.vaskozov.lab4.lib.Result;

public interface AuthorizationInterface {
    Result<UserData, String> authorize(Login login, Password password);

    Result<UserData, String> register(Login login, Password password, String role);

    String createToken(UserData user);

    Result<String, Exception> validateToken(String token);
}
