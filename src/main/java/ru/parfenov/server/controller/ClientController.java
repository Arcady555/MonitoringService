package ru.parfenov.server.controller;

import ru.parfenov.server.consoleview.DataConsoleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientController {
    private final DataConsoleView dataConsoleView;

    public ClientController(DataConsoleView dataConsoleView) {
        this.dataConsoleView = dataConsoleView;
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
                case "0" -> dataConsoleView.inputForSubmitData(login);
                case "1" -> dataConsoleView.viewLastData(login);
                case "2" -> dataConsoleView.inputForViewDataForSpecMonth(login);
                case "3" -> dataConsoleView.viewDataHistory(login);
                case "4" -> {
                    dataConsoleView.out(login);
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}