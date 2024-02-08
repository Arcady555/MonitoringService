package ru.parfenov.server;

import ru.parfenov.server.consoleview.PointValueConsoleView;
import ru.parfenov.server.consoleview.UserConsoleView;
import ru.parfenov.server.serverinterface.Authentication;
import ru.parfenov.server.serverinterface.Registration;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.store.PointValueStore;
import ru.parfenov.server.store.SqlPointValueStore;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилищ и сервисов
     */
    private final UserStore userStore = new SqlUserStore();
    private final PointValueStore pointValueStore = new SqlPointValueStore();
    private final PointValueService pointValueService = new PointValueService(userStore, pointValueStore);
    private final PointValueConsoleView pointValueConsoleView = new PointValueConsoleView(pointValueService);
    private final UserService userService = new UserService(userStore);
    private final UserConsoleView userConsoleView = new UserConsoleView(userService, pointValueConsoleView);

    public ServerClass() throws Exception {
    }

    /**
     * регистрация
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        Registration registration = new Registration(userConsoleView);
        registration.toReg();
    }

    /**
     * авторизация
     *
     * @throws IOException
     */

    public void auth() throws IOException {
        Authentication authentication = new Authentication(userConsoleView, pointValueConsoleView);
        authentication.toAuth();
    }
}