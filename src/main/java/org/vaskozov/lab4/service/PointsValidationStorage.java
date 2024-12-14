package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.CheckResult;
import org.vaskozov.lab4.bean.User;

import java.util.List;

@Singleton
public class PointsValidationStorage implements PointsValidationStorageInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager entityManager;

    @Override
    public boolean save(String login, CheckResult pointCheckResult) {
        try {
            doSave(login, pointCheckResult);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<CheckResult> getAllValidations(String login) {
        TypedQuery<CheckResult> query = entityManager.createQuery(
                "SELECT u FROM CheckResult u WHERE u.userId = :userId",
                CheckResult.class);


        return query.setParameter("userId", getUserIdByLogin(login))
                .getResultList();
    }

    @Override
    public void removeAll(String login) {
        entityManager.createQuery("DELETE CheckResult u WHERE u.userId = :userId")
                .setParameter("userId", getUserIdByLogin(login))
                .executeUpdate();
    }

    private long getUserIdByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.login = :login",
                User.class);

        return query.setParameter("login", login)
                .getSingleResult()
                .getId();
    }

    private void doSave(String login, CheckResult pointCheckResult) {
        long userId = getUserIdByLogin(login);

        CheckResult checkResult = CheckResult.builderWithUserId()
                .userId(userId)
                .x(pointCheckResult.getX())
                .y(pointCheckResult.getY())
                .r(pointCheckResult.getR())
                .inArea(pointCheckResult.isInArea())
                .executionTimeNs(pointCheckResult.getExecutionTimeNs())
                .build();

        entityManager.persist(checkResult);
    }
}
