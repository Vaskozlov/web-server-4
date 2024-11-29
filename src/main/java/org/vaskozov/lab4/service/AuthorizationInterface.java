package org.vaskozov.lab4.service;

import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;

public interface AuthorizationInterface {
    boolean authorize(Login login, Password password);

    boolean register(Login login, Password password);
}
