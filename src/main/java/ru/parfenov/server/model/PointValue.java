package ru.parfenov.server.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PointValue {
    private Long id;
    private Integer userId;
    private LocalDateTime date;
    private String point;
    private Integer value;

    public PointValue() {
    }

    public PointValue(String point, int value) {
        this.point = point;
        this.value = value;
    }

    public PointValue(long id, int userId, LocalDateTime date, String point, int value) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.point = point;
        this.value = value;
    }
}