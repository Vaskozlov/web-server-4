package org.vaskozov.lab4.bean;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "USERS")
@Data
public class UserData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    public UserData() {
        this.login = "";
        this.password = "";
    }
}