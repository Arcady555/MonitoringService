package ru.parfenov.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointValueDto {
    private long id;
    private int userId;
    private String date;
    private String point;
    private int value;
}
