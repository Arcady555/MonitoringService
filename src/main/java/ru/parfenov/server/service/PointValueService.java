package ru.parfenov.server.service;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.store.PointValueStore;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

public class PointValueService {
    private final UserStore userStore;
    private final PointValueStore pointValueStore;

    public PointValueService(UserStore userStore, PointValueStore pointValueStore) {
        this.userStore = userStore;
        this.pointValueStore = pointValueStore;
    }

    public void submitData(String login, List<PointValue> list) {
        int userId = userStore.getByLogin(login).getId();
        for (PointValue pointValue : list) {
            pointValue.setUserId(userId);
            pointValue.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            pointValueStore.create(pointValue);
        }
        fixTime(userStore, login, "submit data");
    }

    public void viewLastData(String login) {
        Optional<List<PointValue>> data = pointValueStore.getLastData(userStore.getByLogin(login).getId());
        if (data.isEmpty()) {
            System.out.println("No data!!!" + System.lineSeparator());
        } else {
            printDataFromDataStore(data.get());
        }
        fixTime(userStore, login, "view last data");
    }

    public void viewDataForSpecMonth(String login, int month, int year) {
        String dateString;
        if (month < 10) {
            dateString = year + "-0" + month + "-01T01:01:01";
        } else {
            dateString = year + "-" + month + "-01T01:01:01";
        }
        LocalDateTime date = LocalDateTime.parse(dateString);
        Optional<List<PointValue>> data = pointValueStore.getDataForSpecMonth(userStore.getByLogin(login), date);
        if (data.isEmpty()) {
            System.out.println("No data!!!" + System.lineSeparator());
        } else {
            printDataFromDataStore(data.get());
            System.out.println(System.lineSeparator());
        }
        fixTime(userStore, login, "view data for spec month");
    }

    public void viewDataHistory(String login) {
        Optional<List<PointValue>> dataListOptional = pointValueStore.findByUser(userStore.getByLogin(login));
        if (dataListOptional.isPresent()) {
            List<PointValue> dataList = dataListOptional.get();
            printDataFromDataStore(dataList);
        } else {
            System.out.println("No data!!!" + System.lineSeparator());
        }
        fixTime(userStore, login, "view data history");
    }

    public void toOut(String login) {
        fixTime(userStore, login, "out");
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
    public boolean validationOnceInMonth(String login) {
        boolean rsl;
        Optional<List<PointValue>> data = pointValueStore.getLastData(userStore.getByLogin(login).getId());
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