package ru.parfenov.server;

import ru.parfenov.server.controller.Authentication;
import ru.parfenov.server.controller.Registration;
import ru.parfenov.server.service.OperationService;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилища с методами, оно в свою очередь запустит хранилище юзеров
     */
    OperationService service = new OperationService();

    /**
     * регистрация
     * @throws IOException
     */
    public void reg() throws IOException {
        Registration registration = new Registration(service);
        registration.toReg();
    }

    /**
     * авторизация
     * @throws IOException
     */

    public void auth() throws IOException {
        Authentication authentication = new Authentication(service);
        authentication.toAuth();
    }
}
