package ru.parfenov.server.store;

import ru.parfenov.server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.parfenov.server.utility.Utility.loadConnection;

public class SqlUserStore implements UserStore {
    private final Connection connection;

    public SqlUserStore() throws Exception {
        this.connection = loadConnection();
    }

    public SqlUserStore(Connection connection) throws Exception {
        this.connection = connection;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = returnUser(resultSet);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int userId) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = returnUser(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getByLogin(String login) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login = ?")) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = returnUser(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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