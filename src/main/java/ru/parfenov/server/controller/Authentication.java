package ru.parfenov.server.controller;

import ru.parfenov.server.service.OperationService;
import ru.parfenov.utility.Utility;

import java.io.IOException;

/**
 * Авторизация
 * Здесь уже есть разделение пользователей на админа и остальных
 */
public class Authentication {
    private final OperationService service;

    public Authentication(OperationService service) {
        this.service = service;
    }

    public void toAuth() throws IOException {
        String login = service.enter();
        if (login.equals("admin")) {
            AdminController controller = new AdminController(service);
            controller.toOperate();
        } else if (login.equals(Utility.EXIT_WORD)) {
        } else {
            ClientController controller = new ClientController(service);
            controller.toOperate(login);
        }
    }
}