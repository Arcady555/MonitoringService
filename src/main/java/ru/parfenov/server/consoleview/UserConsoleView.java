package ru.parfenov.server.consoleview;

import ru.parfenov.server.service.UserService;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserConsoleView {
    private final UserService userService;
    private final DataConsoleView dataConsoleView;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserConsoleView(UserService userService, DataConsoleView dataConsoleView) {
        this.userService = userService;
        this.dataConsoleView = dataConsoleView;
    }

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void inputForReg() throws IOException {
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
    public String enter() throws IOException {
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
    public void viewAllUsers() {
        userService.viewAllUsers();
    }

    /**
     * Актуальные данные нужного юзера
     *
     * @throws IOException
     */
    public void inputForViewLastDataOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataConsoleView.viewLastData(login);
    }

    /**
     * Данные нужного юзера за нужный месяц
     *
     * @throws IOException
     */
    public void inputDataForSpecMonthOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataConsoleView.inputForViewDataForSpecMonth(login);
    }

    /**
     * Посмотреть все заполненные данные нужного юзера
     *
     * @throws IOException
     */
    public void inputForViewDataHistoryOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        dataConsoleView.viewDataHistory(login);
    }

    /**
     * Слежка за пользователем, фиксация по времени всех его движений на сервере
     *
     * @throws IOException
     */
    public void inputForViewUserHistory() throws IOException {
        System.out.println("Enter login of user");
        String login = reader.readLine();
        userService.viewUserHistory(login);
    }
}