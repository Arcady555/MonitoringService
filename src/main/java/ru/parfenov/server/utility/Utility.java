package ru.parfenov.server.utility;

import ru.parfenov.server.model.MetersData;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Utility {
    public static String ADMIN_PASSWORD = "123";
    public static int MAX_NUMBER_OF_POINTS = 10;
    public static String EXIT_WORD = "exit";
    public static int FIRST_YEAR = 2015;

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
