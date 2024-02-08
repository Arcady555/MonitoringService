package ru.parfenov.server.serverinterface;

import ru.parfenov.server.consoleview.PointValueConsoleView;
import ru.parfenov.server.consoleview.UserConsoleView;
import ru.parfenov.server.utility.Utility;

import java.io.IOException;

/**
 * Авторизация
 * Здесь уже есть разделение пользователей на админа и остальных
 */
public class Authentication {
    private final UserConsoleView userConsoleView;
    private final PointValueConsoleView pointValueConsoleView;

    public Authentication(UserConsoleView userConsoleView, PointValueConsoleView pointValueConsoleView) {
        this.userConsoleView = userConsoleView;
        this.pointValueConsoleView = pointValueConsoleView;
    }

    public void toAuth() throws IOException {
        String login = userConsoleView.enter();
        if (login.equals("admin")) {
            AdminInterface adminInterface = new AdminInterface(userConsoleView);
            adminInterface.toOperate();
        } else if (login.equals(Utility.exitWord)) {
            System.out.println("Exit!");
        } else {
            ClientInterface clientInterface = new ClientInterface(pointValueConsoleView);
            clientInterface.toOperate(login);
        }
    }
}