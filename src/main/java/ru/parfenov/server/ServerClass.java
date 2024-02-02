package ru.parfenov.server;

import ru.parfenov.server.controller.Authentication;
import ru.parfenov.server.controller.Registration;
import ru.parfenov.server.service.DataService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.store.MemMetersDataStore;
import ru.parfenov.server.store.MemUserStore;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилищ и сервисов
     */
    MemUserStore memUserStore = new MemUserStore();
    MemMetersDataStore dataStore = new MemMetersDataStore();
    DataService dataService = new DataService(memUserStore, dataStore);
    UserService userService = new UserService(memUserStore, dataService);

    /**
     * регистрация
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        Registration registration = new Registration(userService);
        registration.toReg();
    }

    /**
     * авторизация
     *
     * @throws IOException
     */

    public void auth() throws IOException {
        Authentication authentication = new Authentication(userService, dataService);
        authentication.toAuth();
    }
}
