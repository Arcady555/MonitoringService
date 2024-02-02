package ru.parfenov.server.controller;

import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
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
                case "0" -> userService.viewAllUsers();
                case "1" -> userService.viewLastDataOfUser();
                case "2" -> userService.viewDataForSpecMonthOfUser();
                case "3" -> userService.viewDataHistoryOfUser();
                case "4" -> userService.viewUserHistory();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}