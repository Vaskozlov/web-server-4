package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.RequestResults;
import org.vaskozov.lab4.bean.UserData;

import java.util.List;

@Singleton
public class PointsValidationStorage implements PointsValidationStorageInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager entityManager;

    @Override
    public boolean save(String login, RequestResults pointCheckResult) {
        try {
            doSave(login, pointCheckResult);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<RequestResults> getAllValidations(String login) {
        TypedQuery<RequestResults> query = entityManager.createQuery(
                "SELECT u FROM RequestResults u WHERE u.userId = :userId",
                RequestResults.class);

        query.setParameter("userId", getUserIdByLogin(login));
        return query.getResultList();
    }

    private long getUserIdByLogin(String login) {
        TypedQuery<UserData> query = entityManager.createQuery(
                "SELECT u FROM UserData u WHERE u.login = :login",
                UserData.class);

        query.setParameter("login", login);
        return query.getSingleResult().getId();
    }

    private void doSave(String login, RequestResults pointCheckResult) {
        long userId = getUserIdByLogin(login);

        RequestResults requestResults = new RequestResults();

        requestResults.setUserId(userId);
        requestResults.setX(pointCheckResult.getX());
        requestResults.setY(pointCheckResult.getY());
        requestResults.setR(pointCheckResult.getR());
        requestResults.setInArea(pointCheckResult.isInArea());
        requestResults.setExecutionTimeNs(pointCheckResult.getExecutionTimeNs());

        entityManager.persist(requestResults);
    }
}
