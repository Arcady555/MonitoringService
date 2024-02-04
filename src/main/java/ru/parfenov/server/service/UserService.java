package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.UserStore;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.parfenov.server.utility.Utility.fixTime;

public class UserService {
    private final UserStore userStore;
    private final DataService dataService;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserService(UserStore memUserStore, DataService dataService) {
        this.userStore = memUserStore;
        this.dataService = dataService;
    }

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        System.out.println("Create name");
        String login = reader.readLine();
        if (userStore.getByLogin(login) != null) {
            System.out.println("User is already exist!");
        } else {
            System.out.println("Create password");
            String password = reader.readLine();
            User user = new User();
            user.setLogin(login);
            user.setPassword(password);
            user.setHistory(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                    + " registration"
                    + System.lineSeparator());
            userStore.create(user);
            System.out.println("OK!" + System.lineSeparator());
        }
    }

    /**
     * Авторизация
     * Есть проверка, что юзера с таким логином нет
     * Есть проверка пароля
     *
     * @return
     * @throws IOException
     */
    public String enter() throws IOException {
        String nameOfMethod = "enter";
        System.out.println("Введите имя (или exit)");
        String login = reader.readLine();
        if (login.equals(Utility.exitWord)) {
            return login;
        } else if (userStore.getByLogin(login) == null) {
            System.out.println("Unknown user!" + System.lineSeparator());
            return Utility.exitWord;
        } else {
            System.out.println("Введите пароль");
            String password = reader.readLine();
            if (password.equals(userStore.getByLogin(login).getPassword())) {
                fixTime(userStore, login, nameOfMethod);
            } else {
                System.out.println("Not correct password!");
                return Utility.exitWord;
            }
            return login;
        }
    }

    /**
     * Данные 5 методов ниже используются только админом
     */

    /**
     * Список всех пользователей
     */
    public void viewAllUsers() {
        for (User user : userStore.getAll()) {
            System.out.println(user.getId() + " " + user.getLogin());
        }
        System.out.println(System.lineSeparator());
    }

    /**
     * Актуальные данные нужного юзера
     *
     * @throws IOException
     */
    public void viewLastDataOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataService.viewLastData(login);
    }

    /**
     * Данные нужного юзера за нужный месяц
     *
     * @throws IOException
     */
    public void viewDataForSpecMonthOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataService.viewDataForSpecMonth(login);
    }

    /**
     * Посмотреть все заполненные данные нужного юзера
     *
     * @throws IOException
     */
    public void viewDataHistoryOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataService.viewDataHistory(login);
    }

    /**
     * Слежка за пользователем, фиксация по времени всех его движений на сервере
     *
     * @throws IOException
     */
    public void viewUserHistory() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        User user = userStore.getByLogin(login);
        System.out.println(user.getHistory());
    }
}