package ru.parfenov.server.utility;

import ru.parfenov.server.model.MetersData;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Utility {
    public static String adminPassword = "123";
    public static int maxNumberOfPoints = 10;
    public static String exitWord = "exit";
    public static int firstYear = 2015;

    public static String fixTime(String nameOfMethod) {
        return (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " "
                + nameOfMethod
                + System.lineSeparator());
    }

    /**
     * Распечатка данных, полученных из хранилища
     *
     * @param data
     */
    public static void printDataFromDataStore(MetersData data) {
        System.out.println(data.getDate());
        for (Map.Entry<String, Integer> point : data.getDataPoints().entrySet()) {
            System.out.println(point.getKey() + ": " + point.getValue());
        }
    }
}
