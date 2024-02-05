package ru.parfenov.server.utility;

import ru.parfenov.server.model.User;
import ru.parfenov.server.store.SqlDataStore;
import ru.parfenov.server.store.UserStore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class Utility {
    public static int maxNumberOfPoints = 10;
    public static String exitWord = "exit";
    public static int firstYear = 2015;

    public static void fixTime(UserStore userStore, String login, String nameOfMethod) {
        String history = (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " "
                + nameOfMethod
                + System.lineSeparator());
        User user = userStore.getByLogin(login);
        String newHistory = user.getHistory() + history;
        userStore.insertUserHistory(user, newHistory);
    }

    public static Connection loadConnection() throws ClassNotFoundException, SQLException {
        var config = new Properties();
        try (InputStream in = SqlDataStore.class.getClassLoader()
                .getResourceAsStream("liquibase.properties")) {
            config.load(in);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        String url = loadSysEnvIfNullThenConfig("JDBC_URL", "url", config);
        String username = loadSysEnvIfNullThenConfig("JDBC_USERNAME", "nameForDocker", config);
        String password = loadSysEnvIfNullThenConfig("JDBC_PASSWORD", "DockerPassword", config);
        String driver = loadSysEnvIfNullThenConfig("JDBC_DRIVER", "driver-class-name", config);
        System.out.println("url=" + url);
        Class.forName(driver);
        return DriverManager.getConnection(
                url, username, password
        );
    }

    private static String loadSysEnvIfNullThenConfig(String sysEnv, String key, Properties config) {
        String value = System.getenv(sysEnv);
        if (value == null) {
            value = config.getProperty(key);
        }
        return value;
    }
}
