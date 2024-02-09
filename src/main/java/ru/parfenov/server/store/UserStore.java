package ru.parfenov.server.store;

import ru.parfenov.server.model.User;

import java.util.List;

public interface UserStore {
    void close() throws Exception;

    List<User> getAll();

    User findById(int userId);

    User getByLogin(String name);

    void create(User user);

    void insertUserHistory(User user, String newHistory);
}
