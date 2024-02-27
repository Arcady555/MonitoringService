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
  /*  private static Connection testConnection;
    private static SqlUserStore userStore;

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
        User user = new User(0, "Arcady", "password", "history");
        userStore.create(user);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        testConnection.close();
    }

    @Test
    void whenCreateAndGetAllThanOk() {
        Assertions.assertEquals(userStore.getAll().get(1).getLogin(), "Arcady");
        Assertions.assertEquals(userStore.getAll().get(1).getPassword(), "password");
        Assertions.assertEquals(userStore.getAll().get(1).getHistory(), "history");
    }

    @Test
    void whenCreateAndGetByLoginThanOk() {
        Assertions.assertEquals(userStore.getByLogin("Arcady").get().getPassword(), "password");
        Assertions.assertEquals(userStore.getByLogin("Arcady").get().getHistory(), "history");

    }

    @Test
    void whenCreateAndFindByIdThanOk() {
        int id = userStore.getByLogin("Arcady").get().getId();
        Assertions.assertEquals(userStore.findById(id).get().getLogin(), "Arcady");
        Assertions.assertEquals(userStore.findById(id).get().getPassword(), "password");
        Assertions.assertEquals(userStore.findById(id).get().getHistory(), "history");
    } */
}