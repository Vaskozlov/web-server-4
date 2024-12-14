package org.vaskozov.lab4.service;

import org.vaskozov.lab4.bean.CheckResult;

import java.util.List;

public interface PointsValidationStorageInterface {
    boolean save(String login, CheckResult pointCheckResult);

    List<CheckResult> getAllValidations(String login);

    void removeAll(String login);
}
