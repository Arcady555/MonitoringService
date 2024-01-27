package ru.parfenov.server.controller;

import ru.parfenov.server.service.OperationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientController {
    private final OperationService service;

    public ClientController(OperationService service) {
        this.service = service;
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
                case "0" -> service.submitData(login);
                case "1" -> service.viewLastData(login);
                case "2" -> service.viewDataForSpecMonth(login);
                case "3" -> service.viewDataHistory(login);
                case "4" -> {
                    service.toOut(login);
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}