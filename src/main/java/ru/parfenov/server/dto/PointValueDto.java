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

    public PointValueDto() {
    }

    public PointValueDto(long id, int userId, String date, String point, int value) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.point = point;
        this.value = value;
    }
}
