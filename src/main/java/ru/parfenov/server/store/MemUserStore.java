package ru.parfenov.server.store;

import ru.parfenov.server.model.User;
import ru.parfenov.server.utility.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MemUserStore {

    private final Map<String, User> userMap = new HashMap<>();

    /**
     * при создании хранилища пользователей автоматически добавляется
     * пользователь с логином admin и паролем 123(Если не менялось значение статической переменной)
     */
    public MemUserStore() {
        create("admin", Utility.adminPassword);
    }

    public Set<Map.Entry<String, User>> getAll() {
        return userMap.entrySet();
    }

    public User getByLogin(String name) {
        return userMap.get(name);
    }

    public User create(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        userMap.put(login, user);
        return user;
    }
}