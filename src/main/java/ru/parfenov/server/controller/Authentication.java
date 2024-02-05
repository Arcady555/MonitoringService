package ru.parfenov.server.controller;

import ru.parfenov.server.consoleview.DataConsoleView;
import ru.parfenov.server.consoleview.UserConsoleView;
import ru.parfenov.server.utility.Utility;

import java.io.IOException;

/**
 * Авторизация
 * Здесь уже есть разделение пользователей на админа и остальных
 */
public class Authentication {
    private final UserConsoleView userConsoleView;
    private final DataConsoleView dataConsoleView;

    public Authentication(UserConsoleView userConsoleView, DataConsoleView dataConsoleView) {
        this.userConsoleView = userConsoleView;
        this.dataConsoleView = dataConsoleView;
    }

    public void toAuth() throws IOException {
        String login = userConsoleView.enter();
        if (login.equals("admin")) {
            AdminController controller = new AdminController(userConsoleView);
            controller.toOperate();
        } else if (login.equals(Utility.exitWord)) {
            System.out.println("Exit!");
        } else {
            ClientController controller = new ClientController(dataConsoleView);
            controller.toOperate(login);
        }
    }
}