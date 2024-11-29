package org.vaskozov.lab4.bean;

import jakarta.ejb.Stateful;
import lombok.Data;

import java.io.Serializable;

@Stateful
@Data
public class AuthorizationInfo implements Serializable {
    private String login;
    private String password;

    AuthorizationInfo() {
        this.login = "";
        this.password = "";

        System.out.println("AuthorizationInfo created");
    }
}
