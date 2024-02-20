package ru.parfenov.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.PointValueStore;
import ru.parfenov.server.store.SqlPointValueStore;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

public class JdbcPointValueService implements PointValueService {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcPointValueService.class.getName());

    @Override
    public void submitData(String login, List<PointValue> list) {
        try {
            UserStore userStore = new SqlUserStore();
            PointValueStore pointValueStore = new SqlPointValueStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            int userId = userOptional.map(User::getId).orElse(-1);
            if (userId != -1) {
                for (PointValue pointValue : list) {
                    pointValue.setUserId(userId);
                    pointValue.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
                    pointValueStore.create(pointValue);
                    fixTime(userStore, login, "submit data");

                }
            } else {
                System.out.println("no user!!!");
            }
            userStore.close();
            pointValueStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
    }

    @Override
    public List<PointValue> viewLastData(String login) {
        List<PointValue> listResult = null;
        try {
            UserStore userStore = new SqlUserStore();
            PointValueStore pointValueStore = new SqlPointValueStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            int userId = userOptional.map(User::getId).orElse(-1);
            if (userId != -1) {
                Optional<List<PointValue>> data = pointValueStore.getLastData(userId);
                if (data.isEmpty()) {
                    System.out.println("No data!!!" + System.lineSeparator());
                } else {
                    listResult = data.get();
                    printDataFromDataStore(listResult);
                }
                fixTime(userStore, login, "view last data");
            } else {
                System.out.println("no user!!!");
            }
            userStore.close();
            pointValueStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return listResult;
    }

    @Override
    public List<PointValue> viewDataForSpecMonth(String login, int month, int year) {
        List<PointValue> listResult = null;
        try {
            UserStore userStore = new SqlUserStore();
            PointValueStore pointValueStore = new SqlPointValueStore();
            String dateString;
            if (month < 10) {
                dateString = year + "-0" + month + "-01T01:01:01";
            } else {
                dateString = year + "-" + month + "-01T01:01:01";
            }
            LocalDateTime date = LocalDateTime.parse(dateString);
            Optional<User> userOptional = userStore.getByLogin(login);
            if (userOptional.isPresent()) {
                Optional<List<PointValue>> data = pointValueStore.getDataForSpecMonth(userOptional.get(), date);
                if (data.isEmpty()) {
                    System.out.println("No data!!!" + System.lineSeparator());
                } else {
                    listResult = data.get();
                    printDataFromDataStore(listResult);
                    System.out.println(System.lineSeparator());

                }
                fixTime(userStore, login, "view data for spec month");
            } else {
                System.out.println("no user!!!");
            }
            userStore.close();
            pointValueStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return listResult;
    }

    @Override
    public List<PointValue> viewDataHistory(String login) {
        List<PointValue> listResult = null;
        try {
            UserStore userStore = new SqlUserStore();
            PointValueStore pointValueStore = new SqlPointValueStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            if (userOptional.isPresent()) {
                Optional<List<PointValue>> dataListOptional = pointValueStore.findByUser(userOptional.get());
                if (dataListOptional.isPresent()) {
                    listResult = dataListOptional.get();
                    printDataFromDataStore(listResult);
                } else {
                    System.out.println("No data!!!" + System.lineSeparator());
                }
                fixTime(userStore, login, "view data history");
            } else {
                System.out.println("no user!!!");
            }
            userStore.close();
            pointValueStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return listResult;
    }

    @Override
    public void toOut(String login) {
        try {
            UserStore userStore = new SqlUserStore();
            fixTime(userStore, login, "out");
            userStore.close();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }

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
    @Override
    public boolean validationOnceInMonth(String login) {
        boolean rsl = false;
        try {
            UserStore userStore = new SqlUserStore();
            PointValueStore pointValueStore = new SqlPointValueStore();
            Optional<User> userOptional = userStore.getByLogin(login);
            if (userOptional.isPresent()) {
                Optional<List<PointValue>> data = pointValueStore.getLastData(userOptional.get().getId());
                if (data.isEmpty()) {
                    rsl = true;
                } else {
                    Month curMonth = LocalDateTime.now().getMonth();
                    Month lastMonth = data.get().get(0).getDate().getMonth();
                    int curYear = LocalDateTime.now().getYear();
                    int lastYear = data.get().get(0).getDate().getYear();
                    rsl = !(curMonth.equals(lastMonth) && curYear == lastYear);
                }
            }
            userStore.close();
            pointValueStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * Распечатка данных, полученных из хранилища
     *
     * @param data
     */
    private void printDataFromDataStore(List<PointValue> data) throws Exception {
        UserStore userStore = new SqlUserStore();
        PointValue firstPoint = data.get(0);
        Optional<User> userOptional = userStore.findById(firstPoint.getUserId());
        if (userOptional.isPresent()) {
            System.out.println(
                    userOptional.get().getLogin()
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
        } else {
            System.out.println("no user!!!");
        }
        userStore.close();
    }
}