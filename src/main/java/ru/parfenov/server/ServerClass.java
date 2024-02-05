package ru.parfenov.server;

import ru.parfenov.server.consoleview.DataConsoleView;
import ru.parfenov.server.consoleview.UserConsoleView;
import ru.parfenov.server.controller.Authentication;
import ru.parfenov.server.controller.Registration;
import ru.parfenov.server.service.DataService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.store.DataStore;
import ru.parfenov.server.store.SqlDataStore;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилищ и сервисов
     */
    private final UserStore userStore = new SqlUserStore();
    private final DataStore dataStore = new SqlDataStore();
    private final DataService dataService = new DataService(userStore, dataStore);
    private final DataConsoleView dataConsoleView = new DataConsoleView(dataService);
    private final UserService userService = new UserService(userStore);
    private final UserConsoleView userConsoleView = new UserConsoleView(userService, dataConsoleView);

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
        Authentication authentication = new Authentication(userConsoleView, dataConsoleView);
        authentication.toAuth();
    }
}