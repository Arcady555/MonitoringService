package ru.parfenov.server.store;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DataStore {
    void create(PointValue pointValue);

    Optional<List<PointValue>> findByUser(User user);

    Optional<List<PointValue>> getLastData(int userId);

    Optional<List<PointValue>> getDataForSpecMonth(User user, LocalDateTime date);
}
