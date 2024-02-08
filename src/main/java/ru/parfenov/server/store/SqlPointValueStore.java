package ru.parfenov.server.store;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.server.utility.Utility.loadConnection;

public class SqlPointValueStore implements PointValueStore {
    private final Connection connection;

    public SqlPointValueStore() throws Exception {
        InputStream in = SqlPointValueStore.class.getClassLoader()
                .getResourceAsStream("db/liquibase.properties");
        this.connection = loadConnection(in);
    }

    public SqlPointValueStore(Connection connection) throws Exception {
        this.connection = connection;
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
            e.printStackTrace();
        }
    }

    @Override
    public Optional<List<PointValue>> findByUser(User user) {
        List<PointValue> points = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM point_value where user_id=?")) {
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
            e.printStackTrace();
        }
        return points.size() != 0 ? Optional.of(points) : Optional.empty();
    }

    @Override
    public Optional<List<PointValue>> getLastData(int userId) {
        List<PointValue> points = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT *"
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
            e.printStackTrace();
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