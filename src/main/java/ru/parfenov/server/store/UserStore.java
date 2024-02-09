package ru.parfenov.server.store;

import ru.parfenov.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStore {
    void close() throws Exception;

    List<User> getAll();

    Optional<User> findById(int userId);

    Optional<User> getByLogin(String name);

    void create(User user);

    void insertUserHistory(User user, String newHistory);
}
