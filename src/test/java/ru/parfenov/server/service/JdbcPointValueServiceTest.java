package ru.parfenov.server.service;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.PointValueStore;
import ru.parfenov.server.store.SqlPointValueStore;
import ru.parfenov.server.store.SqlUserStore;
import ru.parfenov.server.store.UserStore;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

@Testcontainers
class JdbcPointValueServiceTest {
/*    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("monitoring_service")
            .withUsername("user")
            .withPassword("pass")
            .withInitScript("changelog/01_ddl_create_table_users_and_data.xml")
            .withInitScript("changelog/02_dml_insert_admin_into_users.xml");
    private static Connection testConnection;
    private static SqlUserStore userStore;
    private static SqlPointValueStore pointValueStore;
    private static PointValueServiceForTest pointValueService;
    private static User user;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeAll
    public static void initConnection() throws Exception {
        testConnection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        userStore = new SqlUserStore(testConnection);
        pointValueStore = new SqlPointValueStore(testConnection);
        pointValueService = new PointValueServiceForTest(pointValueStore, userStore);
        user = new User(0, "Arcady", "123");
        userStore.create(user);
        List<PointValue> list = List.of(
                new PointValue(1,
                        userStore.getByLogin("Arcady").get().getId(),
                        LocalDateTime.of(2024, 1, 1, 12, 0),
                        "heating",
                        100),
                new PointValue(2,
                        userStore.getByLogin("Arcady").get().getId(),
                        LocalDateTime.of(2024, 1, 1, 12, 0),
                        "cool water",
                        200),
                new PointValue(2,
                        userStore.getByLogin("Arcady").get().getId(),
                        LocalDateTime.of(2024, 1, 1, 12, 0),
                        "hot water",
                        300)
        );
        pointValueService.submitData("Arcady", list);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        testConnection.close();
    }

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void whenSubmitDataThanOk() {
        Assertions.assertEquals(pointValueStore.findByUser(user).get().get(0).getPoint(), "heating");
        Assertions.assertEquals(pointValueStore.findByUser(user).get().get(1).getValue(), 200);
    }

    @Test
    void whenViewLastDataThanOk() {
        pointValueService.viewLastData("Arcady");
        Assertions.assertEquals("Arcady"
                        + System.lineSeparator()
                        + "2024-01-01T12:00"
                        + System.lineSeparator()
                        + "heating 100"
                        + System.lineSeparator()
                        + "cool water 200"
                        + System.lineSeparator()
                        + "hot water 300",
                outContent.toString());
    }

    @Test
    void whenViewDataForSpecMonthThanOk() {
        pointValueService.viewDataForSpecMonth("Arcady", 1, 2024);
        Assertions.assertEquals("Arcady"
                        + System.lineSeparator()
                        + "2024-01-01T12:00"
                        + System.lineSeparator()
                        + "heating 100"
                        + System.lineSeparator()
                        + "cool water 200"
                        + System.lineSeparator()
                        + "hot water 300",
                outContent.toString());
    }

    private static class PointValueServiceForTest extends JdbcPointValueService {
        private final SqlPointValueStore pointValueStoreForTest;
        private final SqlUserStore userStoreForTest;

        public PointValueServiceForTest(SqlPointValueStore pointValueStoreForTest, SqlUserStore userStoreForTest) {
            this.pointValueStoreForTest = pointValueStoreForTest;
            this.userStoreForTest = userStoreForTest;
        }

        @Override
        public void submitData(String login, List<PointValue> list) {
            try {
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
                throw new RuntimeException(e);
            }
        }

        @Override
        public List<PointValue> viewLastData(String login) {
            List<PointValue> listResult = null;
            try {
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
                e.printStackTrace();
            }
            return listResult;
        }

        @Override
        public List<PointValue> viewDataForSpecMonth(String login, int month, int year) {
            List<PointValue> listResult = null;
            try {
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
                e.printStackTrace();
            }
            return listResult;
        }

        @Override
        public List<PointValue> viewDataHistory(String login) {
            List<PointValue> listResult = null;
            try {
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
                e.printStackTrace();
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
                e.printStackTrace();
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
    /*    @Override
        public boolean validationOnceInMonth(String login) {
            boolean rsl = false;
            try {
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
     /*   private void printDataFromDataStore(List<PointValue> data) throws Exception {
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
    } */
}
