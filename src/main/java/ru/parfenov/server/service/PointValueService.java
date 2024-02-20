package ru.parfenov.server.service;

import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.model.PointValue;

import java.util.List;

public interface PointValueService {
    void submitData(String login, List<PointValue> list);

    List<PointValueDto> viewLastData(String login);

    List<PointValueDto> viewDataForSpecMonth(String login, int month, int year);

    List<PointValueDto> viewDataHistory(String login);

    void toOut(String login);

  //  boolean validationOnceInMonth(String login);
}
