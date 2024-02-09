package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

public class JdbcUserService implements UserService {

    @Override
    public void reg(String login, String password) {
        try {
            UserStore userStore = new SqlUserStore();
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setHistory(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                    + " registration"
                    + System.lineSeparator());
            userStore.create(user);
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String enter(String login) {
        try {
            UserStore userStore = new SqlUserStore();
            fixTime(userStore, login, "enter");
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return login;
    }

    @Override
    public void viewAllUsers() {
        try {
            UserStore userStore = new SqlUserStore();
            for (User user : userStore.getAll()) {
                System.out.println(user.getId() + " " + user.getLogin());
            }
            userStore.close();
            System.out.println(System.lineSeparator());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewUserHistory(String login) {
        try {
            UserStore userStore = new SqlUserStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            if (userOptional.isPresent()) {
                System.out.println(userOptional.get().getHistory());
            } else {
                System.out.println("no user!");
            }
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getByLogin(String login) {
        User user = null;
        try {
            UserStore userStore = new SqlUserStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            user = userOptional.orElse(null);
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}