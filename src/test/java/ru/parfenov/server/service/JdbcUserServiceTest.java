package ru.parfenov.server.service;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.SqlUserStore;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.fixTime;

@Testcontainers
class JdbcUserServiceTest {
    private static Connection testConnection;
    private static SqlUserStore userStore;
    private static UserServiceForTest userService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    ;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("monitoring_service")
            .withUsername("user")
            .withPassword("pass")
            .withInitScript("changelog/01_ddl_create_table_users_and_data.xml")
            .withInitScript("changelog/02_dml_insert_admin_into_users.xml");

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
        userService = new UserServiceForTest(userStore);
        userService.reg("Arcady", "123");
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
    void whenRegThanOk() {
        Assertions.assertEquals(userStore.getByLogin("Arcady").get().getPassword(), "123");
        Assertions.assertEquals(userStore.getAll().get(1).getLogin(), "Arcady");
        Assertions.assertEquals(userStore.getAll().get(1).getPassword(), "123");
    }

    @Test
    void whenViewAllUsersThanOk() {
        userService.viewAllUsers();
        Assertions.assertEquals("2 admin"
                        + System.lineSeparator()
                        + "2 Arcady",
                outContent.toString());
    }

    @Test
    void whenViewUserHistoryThanOk() {
        userService.viewUserHistory("Arcady");
        Assertions.assertTrue(outContent.toString().contains("registration"));
    }

    @Test
    void whenGetByLoginThanOk() {
        Assertions.assertEquals(userService.getByLogin("Arcady").getPassword(), "123");
    }

    @Test
    void whenGetByLoginThanNot() {
        userService.getByLogin("NotArcady");
        Assertions.assertEquals("no user!", outContent.toString());
    }

    private static class UserServiceForTest extends JdbcUserService {
        private final SqlUserStore userStoreForTest;

        public UserServiceForTest(SqlUserStore userStoreForTest) {
            this.userStoreForTest = userStoreForTest;
        }

        @Override
        public void reg(String login, String password) {
            try {
                User user = new User();
                user.setLogin(login);
                user.setPassword(password);
                user.setHistory(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                        + " registration"
                        + System.lineSeparator());
                userStoreForTest.create(user);
                userStoreForTest.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String enter(String login) {
            try {
                fixTime(userStoreForTest, login, "enter");
                userStoreForTest.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return login;
        }

        @Override
        public void viewAllUsers() {
            try {
                for (User user : userStoreForTest.getAll()) {
                    System.out.println(user.getId() + " " + user.getLogin());
                }
                userStoreForTest.close();
                System.out.println(System.lineSeparator());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void viewUserHistory(String login) {
            try {
                Optional<User> userOptional = userStoreForTest.getByLogin(login);
                if (userOptional.isPresent()) {
                    System.out.println(userOptional.get().getHistory());
                } else {
                    System.out.println("no user!");
                }
                userStoreForTest.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public User getByLogin(String login) {
            User user = null;
            try {
                Optional<User> userOptional = userStoreForTest.getByLogin(login);
                user = userOptional.orElse(null);
                userStoreForTest.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
    }
}