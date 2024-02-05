package ru.parfenov.server.controller;

import ru.parfenov.server.consoleview.UserConsoleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdminController {
    private final UserConsoleView userConsoleView;

    public AdminController(UserConsoleView userConsoleView) {
        this.userConsoleView = userConsoleView;
    }

    public void toOperate() throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("""
                    What operation?
                    0 - view of all users
                    1 - view actual data of user
                    2 - view data for a specific month of user
                    3 - view history of data of user
                    4 - view history of user
                    5 - exit
                    """);
            String answer = r.readLine();
            switch (answer) {
                case "0" -> userConsoleView.viewAllUsers();
                case "1" -> userConsoleView.inputForViewLastDataOfUser();
                case "2" -> userConsoleView.inputDataForSpecMonthOfUser();
                case "3" -> userConsoleView.inputForViewDataHistoryOfUser();
                case "4" -> userConsoleView.inputForViewUserHistory();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}