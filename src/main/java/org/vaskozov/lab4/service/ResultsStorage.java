package org.vaskozov.lab4.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.vaskozov.lab4.bean.CheckResult;
import org.vaskozov.lab4.bean.User;

import java.util.List;

@Singleton
public class ResultsStorage implements ResultsStorageInterface {
    @PersistenceContext(name = "lab4PU")
    private EntityManager database;

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
    public List<CheckResult> getAllResults(String login) {
        TypedQuery<CheckResult> query = database.createQuery(
                "SELECT u FROM CheckResult u WHERE u.userId = :userId",
                CheckResult.class);


        return query.setParameter("userId", getUserId(login))
                .getResultList();
    }

    @Override
    public void removeAll(String login) {
        database.createQuery("DELETE CheckResult u WHERE u.userId = :userId")
                .setParameter("userId", getUserId(login))
                .executeUpdate();
    }

    private long getUserId(String login) {
        TypedQuery<User> query = database.createQuery(
                "SELECT u FROM User u WHERE u.login = :login",
                User.class);

        return query
                .setParameter("login", login)
                .getSingleResult()
                .getId();
    }

    private void doSave(String login, CheckResult pointCheckResult) {
        CheckResult checkResult = CheckResult
                .builderWithUserId()
                .userId(getUserId(login))
                .x(pointCheckResult.getX())
                .y(pointCheckResult.getY())
                .r(pointCheckResult.getR())
                .inArea(pointCheckResult.isInArea())
                .executionTimeNs(pointCheckResult.getExecutionTimeNs())
                .build();

        database.persist(checkResult);
    }
}
