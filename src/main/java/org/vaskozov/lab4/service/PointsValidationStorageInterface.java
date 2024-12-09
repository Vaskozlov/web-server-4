package org.vaskozov.lab4.service;

import org.vaskozov.lab4.bean.RequestResults;

import java.util.List;

public interface PointsValidationStorageInterface {
    boolean save(String login, RequestResults pointCheckResult);

    List<RequestResults> getAllValidations(String login);

    boolean removeAll(String login);
}
