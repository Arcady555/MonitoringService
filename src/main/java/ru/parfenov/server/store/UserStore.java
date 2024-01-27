package ru.parfenov.server.store;

import ru.parfenov.server.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserStore {

    private final Map<String, User> userMap = new HashMap<>();

    public UserStore() {
        create("admin", "123");
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