package ru.parfenov.server.controller;

import ru.parfenov.server.service.DataService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.utility.Utility;

import java.io.IOException;

/**
 * Авторизация
 * Здесь уже есть разделение пользователей на админа и остальных
 */
public class Authentication {
    private final UserService userService;
    private final DataService dataService;

    public Authentication(UserService userService, DataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
    }

    public void toAuth() throws IOException {
        String login = userService.enter();
        if (login.equals("admin")) {
            AdminController controller = new AdminController(userService);
            controller.toOperate();
        } else if (login.equals(Utility.EXIT_WORD)) {
        } else {
            ClientController controller = new ClientController(dataService);
            controller.toOperate(login);
        }
    }
}