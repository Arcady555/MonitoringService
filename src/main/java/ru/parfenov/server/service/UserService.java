package ru.parfenov.server.service;

import ru.parfenov.server.model.User;

import java.util.List;

public interface UserService {
    void reg(String login, String password);

    String enter(String login);

    List<User> viewAllUsers();

    String viewUserHistory(String login);

    User getByLogin(String login);
}
