package ru.parfenov.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.dto.UserToDtoMapper;
import ru.parfenov.server.dto.UserToDtoMapperImpl;
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
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUserService.class.getName());

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
            LOG.error("Exception:", e);
        }
    }

    @Override
    public String enter(String login) {
        try {
            UserStore userStore = new SqlUserStore();
            fixTime(userStore, login, "enter");
            userStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return login;
    }

    @Override
    public List<UserDto> viewAllUsers() {
        UserToDtoMapper userToDtoMapper = new UserToDtoMapperImpl();
        List<UserDto> listDto = new ArrayList<>();
        try {
            UserStore userStore = new SqlUserStore();
            List<User> list = userStore.getAll();
            for (User user : list) {
                UserDto userDto = userToDtoMapper.toUserDto(user);
                listDto.add(userDto);
            }
            userStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return listDto;
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
            LOG.error("Exception:", e);
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
            LOG.error("Exception:", e);
        }
        return user;
    }
}