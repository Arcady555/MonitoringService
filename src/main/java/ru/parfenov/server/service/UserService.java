package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.parfenov.server.utility.Utility.fixTime;

public class UserService {

    public void reg(String login, String password) throws Exception {
        UserStore userStore = new SqlUserStore();
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setHistory(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " registration"
                + System.lineSeparator());
        userStore.create(user);
        userStore.close();
    }

    public String enter(String login) throws Exception {
        UserStore userStore = new SqlUserStore();
        fixTime(userStore, login, "enter");
        userStore.close();
        return login;
    }

    public void viewAllUsers() throws Exception {
        UserStore userStore = new SqlUserStore();
        for (User user : userStore.getAll()) {
            System.out.println(user.getId() + " " + user.getLogin());
        }
        userStore.close();
        System.out.println(System.lineSeparator());
    }

    public void viewUserHistory(String login) throws Exception {
        UserStore userStore = new SqlUserStore();
        User user = userStore.getByLogin(login);
        System.out.println(user.getHistory());
        userStore.close();
    }

    public User getByLogin(String login) throws Exception {
        UserStore userStore = new SqlUserStore();
        User user = userStore.getByLogin(login);
        userStore.close();
        return user;
    }
}