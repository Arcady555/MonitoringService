package ru.parfenov.server.service;

import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.MetersDataStore;
import ru.parfenov.server.store.UserStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OperationService {
    MetersDataStore dataStore = new MetersDataStore();
    UserStore userStore = new UserStore();
    StringBuilder detailsOfVisit = new StringBuilder();
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Данные 7 методов ниже имеют фиксацию времени
     * Все они(кроме первых 2х) используются только клиентом(не админом)
     */

    /**
     * регистрация
     * есть проверка, что юзер с таким логином уже есть
     *
     * @throws IOException
     */
    public void reg() throws IOException {
        System.out.println("Create name");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String login = r.readLine();
        if (userStore.getByLogin(login) != null) {
            System.out.println("User is already exist!\n");
        } else {
            System.out.println("Create password");
            String password = r.readLine();
            User user = userStore.create(login, password);
            dataStore.createDataList(user);
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
        String login = r.readLine();
        if (login.equals("exit")) {
            return login;
        } else if (userStore.getByLogin(login) == null) {
            System.out.println("Unknown user!\n");
            return "exit";
        } else {
            System.out.println("Введите пароль");
            String password = r.readLine();
            if (password.equals(userStore.getByLogin(login).getPassword())) {
                detailsOfVisit.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                        .append(" ")
                        .append("enter")
                        .append(System.lineSeparator());
            } else {
                System.out.println("Not correct password!\n");
                return "exit";
            }
            return login;
        }
    }

    /**
     * Отсылка данных
     * Разрешена только в текущем месяце, один раз. Без редактирования. Проверку выполняет метод validationOnceInMonth()
     * Допускается кроме 3х стандартных значений, ввести ещё свои какие-то показатели и заполнить их.
     *
     * @param login
     * @throws IOException
     */
    public void submitData(String login) throws IOException {
        if (validationOnceInMonth(login)) {
            System.out.println("""
                    1 - Would you like to enter 3 points(heating, cool water, hot water) ?
                    2 - Would you like to enter more points ?
                    """);
            String answer = r.readLine();
            MetersData metersData = new MetersData();
            Map<String, Integer> data = metersData.getDataPoints();
            if (answer.equals("1")) {
                printDataForSubmit(3, data, r);
            } else if (answer.equals("2")) {
                System.out.println("how many points will you create more?");
                int answer2 = Integer.parseInt(r.readLine());
                if (answer2 > 10) {
                    System.out.println("It is too much!!! (Must be not over 10)");
                    submitData(login);
                } else {
                    printDataForSubmit(3 + answer2, data, r);
                }
            } else {
                System.out.println("Please enter correct" + System.lineSeparator());
                submitData(login);
            }
            metersData.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            dataStore.createData(userStore.getByLogin(login), metersData);
            System.out.println("OK!" + System.lineSeparator());
            detailsOfVisit.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                    .append(" ")
                    .append("submit data")
                    .append(System.lineSeparator());
        } else {
            System.out.println("This month data is already exist!!!" + System.lineSeparator());
        }
    }

    /**
     * Просмотр последних заполненных данных
     *
     * @param login
     */

    public void viewLastData(String login) {
        Optional<MetersData> data = dataStore.getLastData(userStore.getByLogin(login));
        if (data.isEmpty()) {
            System.out.println("No data!!!" + System.lineSeparator());
        } else {
            printDataFromDataStore(data.get());
        }
        detailsOfVisit.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .append(" ")
                .append("view last data")
                .append(System.lineSeparator());
    }

    /**
     * Просмотр данных за выбранный месяц
     *
     * @param login
     * @throws IOException
     */
    public void viewDataForSpecMonth(String login) throws IOException {

        System.out.println("""
                Which year are You interesting?
                (Please enter the number 2015-2024)
                """);
        int year = Integer.parseInt(r.readLine());
        if (year > 2024 || year < 2015) {
            System.out.println("Please enter correct" + System.lineSeparator());
            viewDataForSpecMonth(login);
        } else {
            System.out.println("""
                    Which month are You interesting?
                    (Please enter the number 01-12)
                    """);
            int month = Integer.parseInt(r.readLine());
            if (month > 12 || month < 1) {
                System.out.println("Please enter correct" + System.lineSeparator());
                viewDataForSpecMonth(login);
            } else {
                String dateString;
                if (month < 10) {
                    dateString = year + "-0" + month + "-01T01:01:01";
                } else {
                    dateString = year + "-" + month + "-01T01:01:01";
                }
                LocalDateTime date = LocalDateTime.parse(dateString);
                Optional<MetersData> data = dataStore.getDataForSpecMonth(userStore.getByLogin(login), date);
                if (data.isEmpty()) {
                    System.out.println("No data!!!" + System.lineSeparator());
                } else {
                    printDataFromDataStore(data.get());
                    System.out.println(System.lineSeparator());
                }
            }
        }
        detailsOfVisit.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .append(" ")
                .append("view data for spec month")
                .append(System.lineSeparator());
    }

    /**
     * Просмотр всех своих заполненных данных
     *
     * @param login
     */
    public void viewDataHistory(String login) {
        Optional<List<MetersData>> dataListOptional = dataStore.findByUser(userStore.getByLogin(login));
        if (dataListOptional.isPresent()) {
            List<MetersData> dataList = dataListOptional.get();
            for (MetersData metersData : dataList) {
                printDataFromDataStore(metersData);
            }
        }
    }

    public void toOut(String login) {
        detailsOfVisit.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .append(" ")
                .append("out").
                append(System.lineSeparator());
        List<String> history = userStore.getByLogin(login).getHistory();
        history.add(detailsOfVisit.toString());
    }

    /**
     * Данные 5 методов ниже используются только админом
     */
    public void viewAllUsers() {
        for (Map.Entry<String, User> data : userStore.getAll()) {
            System.out.println(data.getKey());
        }
        System.out.println(System.lineSeparator());
    }

    /**
     * @throws IOException
     */
    public void viewLastDataOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = r.readLine();
        viewLastData(login);
    }

    /**
     * @throws IOException
     */
    public void viewDataForSpecMonthOfUser() throws IOException {
        System.out.println("Enter login of user");
        String login = r.readLine();
        viewDataForSpecMonth(login);
    }

    /**
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

    /**
     * Выполняет проверку в методе submitData()
     *
     * @param login
     * @return
     */
    private boolean validationOnceInMonth(String login) {
        boolean rsl;
        Optional<MetersData> data = dataStore.getLastData(userStore.getByLogin(login));
        if (data.isEmpty()) {
            rsl = true;
        } else {
            Month curMonth = LocalDateTime.now().getMonth();
            Month lastMonth = data.get().getDate().getMonth();
            int curYear = LocalDateTime.now().getYear();
            int lastYear = data.get().getDate().getYear();
            rsl = !(curMonth.equals(lastMonth) && curYear == lastYear);
        }
        return rsl;
    }

    /**
     * Помогает сформировать данные для отправки в некоторых методах выше
     *
     * @param pointNumber
     * @param data
     * @param r
     * @throws IOException
     */
    private void printDataForSubmit(int pointNumber, Map<String, Integer> data, BufferedReader r) throws IOException {
        List<String> pointList = new ArrayList<>();
        pointList.add("heating");
        pointList.add("cool water");
        pointList.add("hot water");
        if (pointNumber > 3) {
            for (int i = 4; i < pointNumber + 1; i++) {
                System.out.println("Enter name of " + i + "  point");
                String answer = r.readLine();
                pointList.add(answer);
            }
        }
        for (String s : pointList) {
            System.out.println("Enter " + s + " value");
            int value = Integer.parseInt(r.readLine());
            data.put(s, value);
        }
    }

    private void printDataFromDataStore(MetersData data) {
        System.out.println(data.getDate());
        for (Map.Entry<String, Integer> point : data.getDataPoints().entrySet()) {
            System.out.println(point.getKey() + ": " + point.getValue());
        }
    }
}
