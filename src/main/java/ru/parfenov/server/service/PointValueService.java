package ru.parfenov.server.service;

import ru.parfenov.server.model.PointValue;

import java.util.List;

public interface PointValueService {
    void submitData(String login, List<PointValue> list);

    void viewLastData(String login);

    void viewDataForSpecMonth(String login, int month, int year);

    void viewDataHistory(String login);

    void toOut(String login);

    boolean validationOnceInMonth(String login);
}
