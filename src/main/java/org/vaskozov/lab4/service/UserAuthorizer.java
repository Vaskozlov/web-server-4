package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.User;
import org.vaskozov.lab4.lib.*;

import java.util.List;

@Singleton
public class UserAuthorizer implements UserAuthorizationInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager database;

    @Override
    public Result<User, String> authorize(Login login, Password password) {
        TypedQuery<User> query = database.createQuery(
                "SELECT u FROM User u WHERE u.login = :login AND u.password = :password",
                User.class
        );

        List<User> resultList = query.setParameter("login", login.toString())
                .setParameter("password", password.getHash())
                .getResultList();

        return resultList.isEmpty()
                ? Result.error("Invalid login or password")
                : Result.success(resultList.get(0));
    }

    @Override
    public Result<User, String> register(Login login, Password password, String role) {
        TypedQuery<User> query = database.createQuery(
                "SELECT u FROM User u WHERE u.login = :login",
                User.class
        );

        boolean hasUser = !query.setParameter("login", login.toString())
                .getResultList()
                .isEmpty();

        if (hasUser) {
            return Result.error("User already exists");
        }

        User user = new User(
                login.toString(),
                password.getHash(),
                role
        );

        database.persist(user);
        return Result.success(user);
    }

    @Override
    public String createToken(User user) {
        return JwtUtil.createToken(user.getLogin(), user.getRole());
    }

    @Override
    public Result<String, JwtError> validateToken(String token) {
        return JwtUtil.validateToken(token);
    }
}
