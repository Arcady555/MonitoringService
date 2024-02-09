package ru.parfenov.server.service;

import ru.parfenov.server.model.User;

public interface UserService {
    void reg(String login, String password);

    String enter(String login);

    void viewAllUsers();

    void viewUserHistory(String login);

    User getByLogin(String login);
}
