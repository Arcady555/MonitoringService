package ru.parfenov.server.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.parfenov.server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.ConnectionUtility.loadConnection;

@Repository
public class SqlUserStore implements UserStore {
    private static final Logger LOG = LoggerFactory.getLogger(SqlUserStore.class.getName());

    private final Connection connection;

    @Autowired
    public SqlUserStore() throws SQLException, ClassNotFoundException {
        connection = loadConnection(SqlUserStore
                .class.getClassLoader()
                .getResourceAsStream("db/liquibase.properties"));
    }

    public SqlUserStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, login, password, history FROM users")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = returnUser(resultSet);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return users;
    }

    @Override
    public Optional<User> findById(int userId) {
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, login, password, history FROM users WHERE id = ?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(returnUser(resultSet));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return user;
    }

    @Override
    public Optional<User> getByLogin(String login) {
        Optional<User> user = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id, login, password, history FROM users WHERE login = ?")) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(returnUser(resultSet));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return user;
    }

    @Override
    public void create(User user) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users(login, password, history) VALUES (?, ?, ?)")
        ) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getHistory());
            statement.execute();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
    }

    @Override
    public void insertUserHistory(User user, String newHistory) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE users SET history = ? WHERE id = ?")
        ) {
            statement.setString(1, newHistory);
            statement.setInt(2, user.getId());
            statement.execute();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
    }

    private User returnUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                resultSet.getString("history"));
    }
}