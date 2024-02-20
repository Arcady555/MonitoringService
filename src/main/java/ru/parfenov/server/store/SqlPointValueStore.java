package ru.parfenov.server.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.ConnectionUtility.loadConnection;

@Repository
public class SqlPointValueStore implements PointValueStore {
    private static final Logger LOG = LoggerFactory.getLogger(SqlPointValueStore.class.getName());
    private final Connection connection;

    @Autowired
    public SqlPointValueStore() throws SQLException, ClassNotFoundException {
        connection = loadConnection(SqlPointValueStore
                .class.getClassLoader()
                .getResourceAsStream("db/liquibase.properties"));
    }

    public SqlPointValueStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void create(PointValue pointValue) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO point_value(user_id, date, point, \"value\") VALUES (?, ?, ?, ?)")
        ) {
            statement.setInt(1, pointValue.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(pointValue.getDate()));
            statement.setString(3, pointValue.getPoint());
            statement.setInt(4, pointValue.getValue());
            statement.execute();
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
    }

    @Override
    public Optional<List<PointValue>> findByUser(User user) {
        List<PointValue> points = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, user_id, date, point, \"value\" FROM point_value where user_id=?")
        ) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    points.add(new PointValue(
                            resultSet.getLong("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getString("point"),
                            resultSet.getInt("value")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return points.size() != 0 ? Optional.of(points) : Optional.empty();
    }

    @Override
    public Optional<List<PointValue>> getLastData(int userId) {
        List<PointValue> points = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, user_id, date, point, \"value\""
                        + "  FROM point_value where user_id=? and date=(SELECT date"
                        + "  FROM point_value where user_id=?"
                        + "  ORDER BY date DESC"
                        + "  LIMIT 1)")) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    points.add(new PointValue(
                            resultSet.getLong("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getTimestamp("date").toLocalDateTime(),
                            resultSet.getString("point"),
                            resultSet.getInt("value")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception:", e);
        }
        return points.size() != 0 ? Optional.of(points) : Optional.empty();
    }

    @Override
    public Optional<List<PointValue>> getDataForSpecMonth(User user, LocalDateTime date) {
        Optional<List<PointValue>> resultOptional = Optional.empty();
        List<PointValue> list = findByUser(user).isPresent() ? findByUser(user).get() : null;
        if (list != null && list.size() != 0) {
            List<PointValue> result = new ArrayList<>();
            for (PointValue pointValue : list) {
                if (pointValue.getDate().getMonth().equals(date.getMonth())
                        && pointValue.getDate().getYear() == date.getYear()) {
                    result.add(pointValue);
                    resultOptional = Optional.of(result);
                }
            }
        }
        return resultOptional;
    }
}