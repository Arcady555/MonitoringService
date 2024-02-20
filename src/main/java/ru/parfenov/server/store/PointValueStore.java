package ru.parfenov.server.store;

import org.springframework.stereotype.Repository;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointValueStore {
    void close() throws Exception;

    void create(PointValue pointValue);

    Optional<List<PointValue>> findByUser(User user);

    Optional<List<PointValue>> getLastData(int userId);

    Optional<List<PointValue>> getDataForSpecMonth(User user, LocalDateTime date);
}
