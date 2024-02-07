package ru.parfenov.server.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;

@Testcontainers
class SqlDataStoreTest {
    private static Connection connection;
    private static SqlDataStore dataStore;
    private static User user;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("monitoring_service")
            .withUsername("postgres")
            .withPassword("password")
            .withInitScript("src/main/resources/changelog/01_ddl_create_table_users_and_data.xml")
            .withInitScript("src/main/resources/changelog/02_dml_insert_admin_into_users.xml");


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
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        dataStore = new SqlDataStore(connection);
        user = new User(1, "Arcady", "password", "history");
        PointValue pointValue = new PointValue(
                0, 1, LocalDateTime.of(2024, Month.JANUARY, 22, 10, 10),
                "hot",
                100);
        dataStore.create(pointValue);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void whenCreateAndFindByUserThanOk() throws Exception {
        Assertions.assertEquals(dataStore.findByUser(user).get().get(0).getUserId(), 1);
        Assertions.assertEquals(dataStore.findByUser(user).get().get(0).getPoint(), "hot");
        Assertions.assertEquals(dataStore.findByUser(user).get().get(0).getValue(), 100);
    }

    @Test
    void whenCreateAndGetLastDataThanOk() {
        Assertions.assertEquals(dataStore.getLastData(1).get().get(0).getUserId(), 1);
        Assertions.assertEquals(dataStore.getLastData(1).get().get(0).getPoint(), "hot");
        Assertions.assertEquals(dataStore.getLastData(1).get().get(0).getValue(), 100);

    }

    @Test
    void whenCreateAndGetDataForSpecMonthThanOk() {
        Assertions.assertEquals(dataStore.getDataForSpecMonth(
                user,
                LocalDateTime.of(2024, Month.JANUARY, 1, 00, 00)
        ).get().get(0).getUserId(), 1);
        Assertions.assertEquals(dataStore.getDataForSpecMonth(
                user,
                LocalDateTime.of(2024, Month.JANUARY, 21, 00, 00)
        ).get().get(0).getPoint(), "hot");
        Assertions.assertEquals(dataStore.getDataForSpecMonth(
                user,
                LocalDateTime.of(2024, Month.JANUARY, 31, 00, 00)
        ).get().get(0).getValue(), 100);
    }
}