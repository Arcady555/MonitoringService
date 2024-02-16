package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
    public List<User> viewAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            UserStore userStore = new SqlUserStore();
            list = userStore.getAll();
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String viewUserHistory(String login) {
        String result = "";
        try {
            UserStore userStore = new SqlUserStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            if (userOptional.isPresent()) {
                result = userOptional.get().getHistory();
                System.out.println(result);
            } else {
                System.out.println("no user!");
            }
            userStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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