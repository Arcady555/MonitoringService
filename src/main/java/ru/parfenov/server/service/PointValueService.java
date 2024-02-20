package ru.parfenov.server.service;

import ru.parfenov.server.model.PointValue;

import java.util.List;

public interface PointValueService {
    void submitData(String login, List<PointValue> list);

    List<PointValue> viewLastData(String login);

    List<PointValue> viewDataForSpecMonth(String login, int month, int year);

    List<PointValue> viewDataHistory(String login);

    void toOut(String login);

    boolean validationOnceInMonth(String login);
}
