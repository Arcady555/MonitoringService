package ru.parfenov.server.utility;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtility {

    private ConnectionUtility() {
    }

    public static Connection loadConnection(InputStream in) throws ClassNotFoundException, SQLException {
        var config = new Properties();
        Connection connection;
        try (in) {
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = loadSysEnvIfNullThenConfig("JDBC_URL", "url", config);
        String username = loadSysEnvIfNullThenConfig("JDBC_USERNAME", "username", config);
        String password = loadSysEnvIfNullThenConfig("JDBC_PASSWORD", "password", config);
        String driver = loadSysEnvIfNullThenConfig("JDBC_DRIVER", "driver-class-name", config);
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    private static String loadSysEnvIfNullThenConfig(String sysEnv, String key, Properties config) {
        String value = System.getenv(sysEnv);
        if (value == null) {
            value = config.getProperty(key);
        }
        return value;
    }
}