package ru.parfenov.server.serverinterface;

import ru.parfenov.server.consoleview.PointValueConsoleView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientInterface {
    private final PointValueConsoleView pointValueConsoleView;

    public ClientInterface(PointValueConsoleView pointValueConsoleView) {
        this.pointValueConsoleView = pointValueConsoleView;
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
                case "0" -> pointValueConsoleView.inputForSubmitData(login);
                case "1" -> pointValueConsoleView.viewLastData(login);
                case "2" -> pointValueConsoleView.inputForViewDataForSpecMonth(login);
                case "3" -> pointValueConsoleView.viewDataHistory(login);
                case "4" -> {
                    pointValueConsoleView.out(login);
                    return;
                }
                default -> System.out.println("Please enter correct" + System.lineSeparator());
            }
        }
    }
}