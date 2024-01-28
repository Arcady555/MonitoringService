package ru.parfenov.server.service;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.MetersDataStore;
import ru.parfenov.server.store.UserStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class OperationService {
    private final MetersDataStore dataStore = new MetersDataStore();
    private final UserStore userStore = new UserStore();
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    ClientService clientService = new ClientService(userStore, dataStore);

    /**
     * Данные 7 методов ниже имеют фиксацию времени
     * Все они(кроме 1го) используются только клиентом(не админом)
     */

    /**
     * Авторизация
     * Есть проверка, что юзера с таким логином нет
     * Есть проверка пароля
     *
     * @return
     * @throws IOException
     */

    public String enter() throws IOException {
        return clientService.enter();
    }

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        clientService.reg();

    }

    /**
     * Отсылка данных
     * Разрешена только в текущем месяце, один раз. Без редактирования.
     * Проверку выполняет метод clientService.validationOnceInMonth()
     * Допускается кроме 3х стандартных значений, ввести ещё свои какие-то показатели и заполнить их.
     *
     * @param login
     * @throws IOException
     */
    public void submitData(String login) throws IOException {
        clientService.submitData(login);
    }

    /**
     * Просмотр последних заполненных данных
     *
     * @param login
     */

    public void viewLastData(String login) {
        clientService.viewLastData(login);
    }

    /**
     * Просмотр данных за выбранный месяц
     *
     * @param login
     * @throws IOException
     */
    public void viewDataForSpecMonth(String login) throws IOException {
        clientService.viewDataForSpecMonth(login);
    }

    /**
     * Просмотр всех своих заполненных данных
     *
     * @param login
     */
    public void viewDataHistory(String login) {
        clientService.viewDataHistory(login);
    }

    public void toOut(String login) {
        clientService.toOut(login);
    }

    /**
     * Данные 5 методов ниже используются только админом
     */

    /**
     * Список всех пользователей
     */
    public void viewAllUsers() {
        for (Map.Entry<String, User> data : userStore.getAll()) {
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
        String login = r.readLine();
        viewLastData(login);
    }

    /**
     * Данные нужного юзера за нужный месяц
     *
     * @throws IOException
     */
    public void viewDataForSpecMonthOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = r.readLine();
        viewDataForSpecMonth(login);
    }

    /**
     * Посмотреть все данные нужного юзера
     *
     * @throws IOException
     */
    public void viewDataHistoryOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = r.readLine();
        viewDataHistory(login);
    }

    /**
     * Слежка за пользователем, фиксация по времени всех его движений на сервере
     *
     * @throws IOException
     */
    public void viewUserHistory() throws IOException {
        System.out.println("Enter login of user");
        String login = r.readLine();
        User user = userStore.getByLogin(login);
        List<String> history = user.getHistory();
        if (history.isEmpty()) {
            System.out.println("Not data!!!" + System.lineSeparator());
        } else {
            for (String data : history) {
                System.out.println(data + System.lineSeparator());
            }
        }
    }
}