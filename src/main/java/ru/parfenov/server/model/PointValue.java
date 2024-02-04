package ru.parfenov.server.model;

import java.time.LocalDateTime;

public class PointValue {
    private long id;
    private int userId;
    private LocalDateTime date;
    private String point;
    private int value;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}