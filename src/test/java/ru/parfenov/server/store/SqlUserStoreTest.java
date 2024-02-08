package ru.parfenov.server.store;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.model.User;

import java.sql.SQLException;

@Testcontainers
class SqlUserStoreTest {
    private static Connection connection;
    private static SqlUserStore userStore;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("monitoring_service")
            .withUsername("user")
            .withPassword("pass")
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
        userStore = new SqlUserStore(connection);
        User user = new User(0, "Arcady", "password", "history");
        userStore.create(user);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    void whenCreateAndGetAllThanOk() {
        Assertions.assertEquals(userStore.getAll().get(1).getLogin(), "Arcady");
        Assertions.assertEquals(userStore.getAll().get(1).getPassword(), "password");
        Assertions.assertEquals(userStore.getAll().get(1).getHistory(), "history");
    }

    @Test
    void whenCreateAndGetByLoginThanOk() {
        Assertions.assertEquals(userStore.getByLogin("Arcady").getPassword(), "password");
        Assertions.assertEquals(userStore.getByLogin("Arcady").getHistory(), "history");

    }

    @Test
    void whenCreateAndFindByIdThanOk() {
        int id = userStore.getByLogin("Arcady").getId();
        Assertions.assertEquals(userStore.findById(id).getLogin(), "Arcady");
        Assertions.assertEquals(userStore.findById(id).getPassword(), "password");
        Assertions.assertEquals(userStore.findById(id).getHistory(), "history");
    }
}