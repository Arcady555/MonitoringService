package ru.parfenov.server.controller;

import ru.parfenov.server.service.DataService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientController {
    private final DataService dataService;

    public ClientController(DataService dataService) {
        this.dataService = dataService;
    }

    public void toOperate(String login) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("""
                    What operation?
                    0 - submission of data
                    1 - view actual data
                    2 - view data for a specific month
                    3 - view the history of your data
                    4 - exit
                    """);
            String answer = r.readLine();
            switch (answer) {
                case "0" -> dataService.submitData(login);
                case "1" -> dataService.viewLastData(login);
                case "2" -> dataService.viewDataForSpecMonth(login);
                case "3" -> dataService.viewDataHistory(login);
                case "4" -> {
                    dataService.toOut(login);
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}