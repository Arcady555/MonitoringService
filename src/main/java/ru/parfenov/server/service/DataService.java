package ru.parfenov.server.service;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.DataStore;
import ru.parfenov.server.store.UserStore;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

/**
 * Данный класс служит для работы с данными пользователем(не админом)
 */

public class DataService {
    private final UserStore userStore;
    private final DataStore dataStore;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public DataService(UserStore userStore, DataStore dataStore) {
        this.userStore = userStore;
        this.dataStore = dataStore;
    }

    /**
     * Данные 5 методов ниже имеют фиксацию времени
     * Используются только клиентом(не админом)
     */

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
        try {
            User user = userStore.getByLogin(login);
            String nameOfMethod = "submit data";
            if (validationOnceInMonth(login)) {
                System.out.println("""
                        1 - Would you like to enter 3 points(heating, cool water, hot water) ?
                        2 - Would you like to enter more points ?
                        """);
                String answer = reader.readLine();
                List<PointValue> list = new ArrayList<>();
                if (answer.equals("1")) {
                    printDataForSubmit(3, list, reader);
                } else if (answer.equals("2")) {
                    System.out.println("how many points will you create more?");

                    int answer2 = Integer.parseInt(reader.readLine());
                    if (answer2 > Utility.maxNumberOfPoints) {
                        System.out.println("It is too much!!! (Must be not over " + Utility.maxNumberOfPoints + ")");
                        submitData(login);
                    } else {
                        printDataForSubmit(3 + answer2, list, reader);
                    }
                } else {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    submitData(login);
                }
                for (PointValue pointValue : list) {
                    pointValue.setUserId(user.getId());
                    pointValue.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    dataStore.create(pointValue);
                }
                System.out.println("OK!" + System.lineSeparator());
                fixTime(userStore, login, nameOfMethod);
            } else {
                System.out.println("This month data is already exist!!!" + System.lineSeparator());
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter correct!!!" + System.lineSeparator());
            submitData(login);
        }
    }

    /**
     * Просмотр последних заполненных данных
     *
     * @param login
     */
    public void viewLastData(String login) {
        String nameOfMethod = "view last data";
        Optional<List<PointValue>> data = dataStore.getLastData(userStore.getByLogin(login).getId());
        if (data.isEmpty()) {
            System.out.println("No data!!!" + System.lineSeparator());
        } else {
            printDataFromDataStore(data.get());
        }
        fixTime(userStore, login, nameOfMethod);
    }

    /**
     * Просмотр данных за выбранный месяц
     *
     * @param login
     * @throws IOException
     */
    public void viewDataForSpecMonth(String login) throws IOException {
        String nameOfMethod = "view data for spec month";
        System.out.println("Which year are You interesting?");
        System.out.println("Please enter the number " + Utility.firstYear + "-" + LocalDateTime.now().getYear());
        int year = Integer.parseInt(reader.readLine());
        if (year > LocalDateTime.now().getYear() || year < Utility.firstYear) {
            System.out.println("Please enter correct" + System.lineSeparator());
            viewDataForSpecMonth(login);
        } else {
            System.out.println("""
                    Which month are You interesting?
                    (Please enter the number 1-12)
                    """);
            try {
                int month = Integer.parseInt(reader.readLine());
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
                    Optional<List<PointValue>> data = dataStore.getDataForSpecMonth(userStore.getByLogin(login), date);
                    if (data.isEmpty()) {
                        System.out.println("No data!!!" + System.lineSeparator());
                    } else {
                        printDataFromDataStore(data.get());
                        System.out.println(System.lineSeparator());
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter correct!!!" + System.lineSeparator());
                viewDataForSpecMonth(login);
            }

        }
        fixTime(userStore, login, nameOfMethod);
    }

    /**
     * Просмотр всех своих заполненных данных
     *
     * @param login
     */
    public void viewDataHistory(String login) {
        String nameOfMethod = "view data history";
        Optional<List<PointValue>> dataListOptional = dataStore.findByUser(userStore.getByLogin(login));
        if (dataListOptional.isPresent()) {
            List<PointValue> dataList = dataListOptional.get();
            printDataFromDataStore(dataList);
        } else {
            System.out.println("No data!!!" + System.lineSeparator());
        }
        fixTime(userStore, login, nameOfMethod);
    }

    public void toOut(String login) {
        String nameOfMethod = "out";
        fixTime(userStore, login, nameOfMethod);
    }

    /**
     * Выполняет проверку в методе submitData(),
     * который может выполняться только один раз и в текущем месяце.
     * Если в текущем месяце уже есть данные(актуальные, последние данные)
     * - значит ждите следующего месяца))
     *
     * @param login
     * @return
     */
    private boolean validationOnceInMonth(String login) {
        boolean rsl;
        Optional<List<PointValue>> data = dataStore.getLastData(userStore.getByLogin(login).getId());
        if (data.isEmpty()) {
            rsl = true;
        } else {
            Month curMonth = LocalDateTime.now().getMonth();
            Month lastMonth = data.get().get(0).getDate().getMonth();
            int curYear = LocalDateTime.now().getYear();
            int lastYear = data.get().get(0).getDate().getYear();
            rsl = !(curMonth.equals(lastMonth) && curYear == lastYear);
        }
        return rsl;
    }

    /**
     * Помогает сформировать данные для отправки в хранилище
     *
     * @param pointNumber
     * @param list
     * @param reader
     * @throws IOException
     */
    private void printDataForSubmit(int pointNumber, List<PointValue> list, BufferedReader reader) throws IOException {
        List<String> pointList = new ArrayList<>();
        pointList.add("heating");
        pointList.add("cool water");
        pointList.add("hot water");
        if (pointNumber > 3) {
            for (int i = 4; i < pointNumber + 1; i++) {
                System.out.println("Enter name of " + i + "  point");
                String answer = reader.readLine();
                pointList.add(answer);
            }
        }
        for (String s : pointList) {
            System.out.println("Enter " + s + " value");
            int value = Integer.parseInt(reader.readLine());
            list.add(new PointValue(s, value));
        }
    }

    /**
     * Распечатка данных, полученных из хранилища
     *
     * @param data
     */
    private void printDataFromDataStore(List<PointValue> data) {
        PointValue firstPoint = data.get(0);
        System.out.println(
                userStore.findById(firstPoint.getUserId()).getLogin()
                        + System.lineSeparator()
                        + firstPoint.getDate()
                        + System.lineSeparator()
                        + firstPoint.getPoint()
                        + " "
                        + firstPoint.getValue()
        );
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).getDate().equals(data.get(i - 1).getDate())) {
                System.out.println(data.get(i).getPoint() + " " + data.get(i).getValue());
            } else {
                System.out.println(
                        data.get(i).getDate()
                                + System.lineSeparator()
                                + data.get(i).getPoint()
                                + " "
                                + data.get(i).getValue()
                );
            }
        }
    }
}