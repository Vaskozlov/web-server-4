package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.UserData;
import org.vaskozov.lab4.lib.JwtUtil;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;
import org.vaskozov.lab4.lib.Result;

import java.util.List;

@Singleton
public class AuthorizationService implements AuthorizationInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager entityManager;

    @Override
    public Result<UserData, String> authorize(Login login, Password password) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login AND u.password = :password",
                UserData.class
        );

        List<UserData> resultList = query.setParameter("login", login.toString())
                .setParameter("password", password.getHash())
                .getResultList();

        return resultList.isEmpty()
                ? Result.error("Invalid login or password")
                : Result.success(resultList.get(0));
    }

    @Override
    public Result<UserData, String> register(Login login, Password password, String role) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login",
                UserData.class
        );

        boolean hasUser = !query.setParameter("login", login.toString())
                .getResultList()
                .isEmpty();

        if (hasUser) {
            return Result.error("User already exists");
        }

        UserData user = new UserData(
                login.toString(),
                password.getHash(),
                role
        );

        entityManager.persist(user);
        return Result.success(user);
    }

    @Override
    public String createToken(UserData user) {
        return JwtUtil.createToken(user.getLogin(), user.getRole());
    }

    @Override
    public Result<String, Exception> validateToken(String token) {
        return JwtUtil.validateToken(token);
    }
}
