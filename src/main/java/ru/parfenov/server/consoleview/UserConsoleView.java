package ru.parfenov.server.consoleview;

import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserConsoleView {
    private final JdbcUserService userService;
    private final PointValueConsoleView pointValueConsoleView;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserConsoleView(JdbcUserService userService, PointValueConsoleView pointValueConsoleView) {
        this.userService = userService;
        this.pointValueConsoleView = pointValueConsoleView;
    }

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void inputForReg() throws Exception {
        System.out.println("Create name");
        String login = reader.readLine();
        if (userService.getByLogin(login) != null) {
            System.out.println("User is already exist!");
        } else {
            System.out.println("Create password");
            String password = reader.readLine();
            userService.reg(login, password);
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
    public String enter() throws Exception {
        System.out.println("Введите имя (или exit)");
        String login = reader.readLine();
        if (login.equals(Utility.exitWord)) {
            return login;
        } else if (userService.getByLogin(login) == null) {
            System.out.println("Unknown user!" + System.lineSeparator());
            return Utility.exitWord;
        } else {
            System.out.println("Введите пароль");
            String password = reader.readLine();
            if (password.equals(userService.getByLogin(login).getPassword())) {
                userService.enter(login);
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
    public void viewAllUsers() throws Exception {
        userService.viewAllUsers();
    }

    /**
     * Актуальные данные нужного юзера
     *
     * @throws IOException
     */
    public void inputForViewLastDataOfUser() throws Exception {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        pointValueConsoleView.viewLastData(login);
    }

    /**
     * Данные нужного юзера за нужный месяц
     *
     * @throws IOException
     */
    public void inputDataForSpecMonthOfUser() throws Exception {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        pointValueConsoleView.inputForViewDataForSpecMonth(login);
    }

    /**
     * Посмотреть все заполненные данные нужного юзера
     *
     * @throws IOException
     */
    public void inputForViewDataHistoryOfUser() throws Exception {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        pointValueConsoleView.viewDataHistory(login);
    }

    /**
     * Слежка за пользователем, фиксация по времени всех его движений на сервере
     *
     * @throws IOException
     */
    public void inputForViewUserHistory() throws Exception {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        userService.viewUserHistory(login);
    }
}