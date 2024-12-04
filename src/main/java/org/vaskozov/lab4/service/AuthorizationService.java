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

        return !query.setParameter("login", login.toString())
                .setParameter("password", password.getHash())
                .getResultList()
                .isEmpty();
    }

    @Override
    public boolean register(Login login, Password password) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login",
                UserData.class
        );

        boolean hasUser = !query.setParameter("login", login.toString())
                .getResultList()
                .isEmpty();

        if (hasUser) {
            return false;
        }

        UserData user = new UserData(
                login.toString(),
                password.getHash()
        );

        entityManager.persist(user);
        return true;
    }
}
