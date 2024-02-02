package ru.parfenov.server.store;

import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;

import java.time.LocalDateTime;
import java.util.*;

public class MemMetersDataStore {
    Map<User, List<MetersData>> store = new HashMap<>();

    public void createDataList(User user) {
        store.put(user, new ArrayList<>());
    }

    public void createData(User user, MetersData metersData) {
        store.get(user).add(metersData);
    }

    public Optional<List<MetersData>> findByUser(User user) {
        return Optional.ofNullable(store.get(user));
    }

    public Optional<MetersData> getLastData(User user) {
        List<MetersData> list = findByUser(user).isPresent() ? findByUser(user).get() : null;
        return list != null && list.size() != 0 ? Optional.of(list.get(list.size() - 1)) : Optional.empty();
    }

    public Optional<MetersData> getDataForSpecMonth(User user, LocalDateTime date) {
        Optional<MetersData> result = Optional.empty();
        List<MetersData> list = findByUser(user).isPresent() ? findByUser(user).get() : null;
        if (list != null && list.size() != 0) {
            for (MetersData element : list) {
                if (element.getDate().getMonth().equals(date.getMonth()) &&
                        element.getDate().getYear() == date.getYear()) {
                    result = Optional.of(element);
                    break;
                }
            }
        }
        return result;
    }
}