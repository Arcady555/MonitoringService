package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.parfenov.server.utility.Utility.fixTime;

public class UserService {
    private final UserStore userStore;

    public UserService(UserStore memUserStore) {
        this.userStore = memUserStore;
    }

    public void reg(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setHistory(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " registration"
                + System.lineSeparator());
        userStore.create(user);
    }

    public String enter(String login) {
        fixTime(userStore, login, "enter");
        return login;
    }

    public void viewAllUsers() {
        for (User user : userStore.getAll()) {
            System.out.println(user.getId() + " " + user.getLogin());
        }
        System.out.println(System.lineSeparator());
    }

    public void viewUserHistory(String login) {
        User user = userStore.getByLogin(login);
        System.out.println(user.getHistory());
    }

    public User getByLogin(String login) {
        return userStore.getByLogin(login);
    }
}