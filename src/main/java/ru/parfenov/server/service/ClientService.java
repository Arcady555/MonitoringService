package ru.parfenov.server.service;

import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.MetersDataStore;
import ru.parfenov.server.store.UserStore;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Данный класс служит для выделения методов для пользователя(не админа)
 */

public class ClientService {
    private final UserStore userStore;
    private final MetersDataStore dataStore;
    private final BufferedReader reader;
    StringBuilder detailsOfVisit = new StringBuilder();

    public ClientService(UserStore userStore, MetersDataStore dataStore, BufferedReader reader) {
        this.userStore = userStore;
        this.dataStore = dataStore;
        this.reader = reader;
    }

    public void reg() throws IOException {
        String nameOfMethod = "registration";
        System.out.println("Create name");
        String login = reader.readLine();
        if (userStore.getByLogin(login) != null) {
            System.out.println("User is already exist!\n");
        } else {
            System.out.println("Create password");
            String password = reader.readLine();
            User user = userStore.create(login, password);
            dataStore.createDataList(user);
        }
        System.out.println("OK!" + System.lineSeparator());
        fixTime(nameOfMethod, detailsOfVisit);
        List<String> history = userStore.getByLogin(login).getHistory();
        history.add(detailsOfVisit.toString());
    }

    public String enter() throws IOException {
        String nameOfMethod = "enter";
        System.out.println("Введите имя (или exit)");
        String login = reader.readLine();
        if (login.equals(Utility.EXIT_WORD)) {
            return login;
        } else if (userStore.getByLogin(login) == null) {
            System.out.println("Unknown user!\n");
            return Utility.EXIT_WORD;
        } else {
            System.out.println("Введите пароль");
            String password = reader.readLine();
            if (password.equals(userStore.getByLogin(login).getPassword())) {
                fixTime(nameOfMethod, detailsOfVisit);
            } else {
                System.out.println("Not correct password!\n");
                return Utility.EXIT_WORD;
            }
            return login;
        }
    }

    public void submitData(String login) throws IOException {
        try {
            String nameOfMethod = "submit data";
            if (validationOnceInMonth(login)) {
                System.out.println("""
                    1 - Would you like to enter 3 points(heating, cool water, hot water) ?
                    2 - Would you like to enter more points ?
                    """);
                String answer = reader.readLine();
                MetersData metersData = new MetersData();
                Map<String, Integer> data = metersData.getDataPoints();
                if (answer.equals("1")) {
                    printDataForSubmit(3, data, reader);
                } else if (answer.equals("2")) {
                    System.out.println("how many points will you create more?");

                    int answer2 = Integer.parseInt(reader.readLine());
                    if (answer2 > Utility.MAX_NUMBER_OF_POINTS) {
                        System.out.println("It is too much!!! (Must be not over " + Utility.MAX_NUMBER_OF_POINTS + ")");
                        submitData(login);
                    } else {
                        printDataForSubmit(3 + answer2, data, reader);
                    }
                } else {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    submitData(login);
                }
                metersData.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                dataStore.createData(userStore.getByLogin(login), metersData);
                System.out.println("OK!" + System.lineSeparator());
                fixTime(nameOfMethod, detailsOfVisit);
            } else {
                System.out.println("This month data is already exist!!!" + System.lineSeparator());
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter correct!!!" + System.lineSeparator());
            submitData(login);
        }
    }

    public void viewLastData(String login) {
        String nameOfMethod = "view last data";
        Optional<MetersData> data = dataStore.getLastData(userStore.getByLogin(login));
        if (data.isEmpty()) {
            System.out.println("No data!!!" + System.lineSeparator());
        } else {
            printDataFromDataStore(data.get());
        }
        fixTime(nameOfMethod, detailsOfVisit);
    }

    public void viewDataForSpecMonth(String login) throws IOException {
        String nameOfMethod = "view data for spec month";
        System.out.println("Which year are You interesting?");
        System.out.println("Please enter the number " + Utility.FIRST_YEAR +"-" + LocalDateTime.now().getYear());
        int year = Integer.parseInt(reader.readLine());
        if (year > LocalDateTime.now().getYear() || year < Utility.FIRST_YEAR) {
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
                    Optional<MetersData> data = dataStore.getDataForSpecMonth(userStore.getByLogin(login), date);
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
        fixTime(nameOfMethod, detailsOfVisit);
    }

    public void viewDataHistory(String login) {
        String nameOfMethod = "view data history";
        Optional<List<MetersData>> dataListOptional = dataStore.findByUser(userStore.getByLogin(login));
        if (dataListOptional.isPresent()) {
            List<MetersData> dataList = dataListOptional.get();
            for (MetersData metersData : dataList) {
                printDataFromDataStore(metersData);
            }
        } else {
            System.out.println("No data!!!" + System.lineSeparator());
        }
        fixTime(nameOfMethod, detailsOfVisit);
    }

    public void toOut(String login) {
        String nameOfMethod = "out";
        fixTime(nameOfMethod, detailsOfVisit);
        List<String> history = userStore.getByLogin(login).getHistory();
        history.add(detailsOfVisit.toString());
    }

    /**
     * Выполняет проверку в методе submitData(),
     * который может выполняться только один раз и в текущем месяце.
     * Если в текущем месяце уже есть данные(актуальные, последние данные)
     * - значит ждите следующего месяца))
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
     * Помогает сформировать данные для отправки в хранилище
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

    /**
     * Распечатка данных, полученных из хранилища
     * @param data
     */
    private void printDataFromDataStore(MetersData data) {
        System.out.println(data.getDate());
        for (Map.Entry<String, Integer> point : data.getDataPoints().entrySet()) {
            System.out.println(point.getKey() + ": " + point.getValue());
        }
    }

    private void fixTime(String nameOfMethod, StringBuilder stringBuilder) {
        stringBuilder.append(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .append(" ")
                .append(nameOfMethod)
                .append(System.lineSeparator());
    }
}