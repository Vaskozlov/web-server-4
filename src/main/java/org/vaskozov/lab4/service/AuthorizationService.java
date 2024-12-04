package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.UserData;
import org.vaskozov.lab4.lib.Login;
import org.vaskozov.lab4.lib.Password;

@Singleton
public class AuthorizationService implements AuthorizationInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager entityManager;

    @Override
    public boolean authorize(Login login, Password password) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login AND u.password = :password",
                UserData.class
        );

        query.setParameter("login", login.toString());
        query.setParameter("password", password.getHash());

        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean register(Login login, Password password) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login",
                UserData.class
        );

        query.setParameter("login", login.toString());

        if (!query.getResultList().isEmpty()) {
            return false;
        }

        UserData user = new UserData();
        user.setLogin(login.toString());
        user.setPassword(password.getHash());
        entityManager.persist(user);

        return true;
    }
}
