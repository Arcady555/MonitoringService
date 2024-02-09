package ru.parfenov.server.utility;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Utility {
    public static int maxNumberOfPoints = 10;
    public static String exitWord = "exit";
    public static int firstYear = 2015;

    public static void fixTime(UserStore userStore, String login, String nameOfMethod) {
        String history = (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " "
                + nameOfMethod
                + System.lineSeparator());
        User user = userStore.getByLogin(login);
        String newHistory = user.getHistory() + history;
        userStore.insertUserHistory(user, newHistory);
    }
}
