package ru.parfenov.server;

import ru.parfenov.server.consoleview.PointValueConsoleView;
import ru.parfenov.server.consoleview.UserConsoleView;
import ru.parfenov.server.serverinterface.Authentication;
import ru.parfenov.server.serverinterface.Registration;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск сервисов
     */
    private final PointValueService pointValueService = new PointValueService();
    private final PointValueConsoleView pointValueConsoleView = new PointValueConsoleView(pointValueService);
    private final UserService userService = new UserService();
    private final UserConsoleView userConsoleView = new UserConsoleView(userService, pointValueConsoleView);

    public ServerClass() {
    }

    /**
     * регистрация
     *
     * @throws IOException
     */
    public void reg() throws Exception {
        Registration registration = new Registration(userConsoleView);
        registration.toReg();
    }

    /**
     * авторизация
     *
     * @throws IOException
     */

    public void auth() throws Exception {
        Authentication authentication = new Authentication(userConsoleView, pointValueConsoleView);
        authentication.toAuth();
    }
}