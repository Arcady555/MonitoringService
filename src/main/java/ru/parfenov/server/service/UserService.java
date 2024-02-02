package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.MemUserStore;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static ru.parfenov.server.utility.Utility.fixTime;

public class UserService {
    private final MemUserStore memUserStore;
    private final DataService dataService;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserService(MemUserStore memUserStore, DataService dataService) {
        this.memUserStore = memUserStore;
        this.dataService = dataService;
    }

    /**
     * Данные 7 методов ниже имеют фиксацию времени
     * Все они используются только клиентом(не админом) (кроме 1го, он используется ещё и админом)
     */

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        String nameOfMethod = "registration";
        System.out.println("Create name");
        String login = reader.readLine();
        if (memUserStore.getByLogin(login) != null) {
            System.out.println("User is already exist!\n");
        } else {
            System.out.println("Create password");
            String password = reader.readLine();
            User user = memUserStore.create(login, password);
            dataService.getDataStore().createDataList(user);
        }
        System.out.println("OK!" + System.lineSeparator());
        memUserStore.getByLogin(login).setHistory(fixTime(nameOfMethod));
    }

    /**
     * Авторизация
     * Есть проверка, что юзера с таким логином нет
     * Есть проверка пароля
     * Admin заходит с паролем 123, если не поменять значение Utility.ADMIN_PASSWORD
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
        } else if (memUserStore.getByLogin(login) == null) {
            System.out.println("Unknown user!\n");
            return Utility.exitWord;
        } else {
            System.out.println("Введите пароль");
            String password = reader.readLine();
            if (password.equals(memUserStore.getByLogin(login).getPassword())) {
                String newHistory = memUserStore.getByLogin(login).getHistory() + fixTime(nameOfMethod);
                memUserStore.getByLogin(login).setHistory(newHistory);
            } else {
                System.out.println("Not correct password!\n");
                return Utility.exitWord;
            }
            return login;
        }
    }

    /**
     * Список всех пользователей
     */
    public void viewAllUsers() {
        for (Map.Entry<String, User> data : memUserStore.getAll()) {
            System.out.println(data.getKey());
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
        User user = memUserStore.getByLogin(login);
        System.out.println(user.getHistory());
    }
}