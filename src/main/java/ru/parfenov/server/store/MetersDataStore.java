package ru.parfenov.server.store;

import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MetersDataStore {
    void createDataList(User user);

    void createData(User user, MetersData metersData);

    Optional<List<MetersData>> findByUser(User user);

    Optional<MetersData> getLastData(User user);

    Optional<MetersData> getDataForSpecMonth(User user, LocalDateTime date);
}
