package ru.parfenov.server.store;

import ru.parfenov.server.model.User;

import java.util.Map;
import java.util.Set;

public interface UserStore {
    Set<Map.Entry<String, User>> getAll();

    User getByLogin(String name);

    User create(String login, String password);
}
