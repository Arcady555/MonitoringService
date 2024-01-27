package ru.parfenov.server.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetersData {
    private LocalDateTime date;
    private Map<String, Integer> dataPoints = new LinkedHashMap<>();

    public MetersData() {
    }

    public MetersData(LocalDateTime date, Map<String, Integer> dataPoints) {
        this.date = date;
        this.dataPoints = dataPoints;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Map<String, Integer> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Map<String, Integer> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void addPoint(String name, int value) {
        dataPoints.put(name, value);
    }
}