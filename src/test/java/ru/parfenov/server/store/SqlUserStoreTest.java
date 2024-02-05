package ru.parfenov.server.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.model.User;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class SqlUserStoreTest {
    private static Connection connection;
    private static SqlUserStore userStore;
    private static User user;

    @BeforeAll
    public static void initConnection() throws Exception {
        try (InputStream in = SqlUserStore.class.getClassLoader()
                .getResourceAsStream("db/liquibase-test.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        userStore = new SqlUserStore(connection);
        user = new User(0, "Arcady", "password", "history");
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