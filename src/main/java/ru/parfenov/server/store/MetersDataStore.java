package ru.parfenov.server.store;

import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;

import java.time.LocalDateTime;
import java.util.*;

public class MetersDataStore {
    Map<User, List<MetersData>> store = new HashMap<>();

    public void createDataList(User user) {
        store.put(user, new ArrayList<>());
    }

    public void createData(User user, MetersData metersData) {
        store.get(user).add(metersData);
    }

    public Optional<List<MetersData>> findByUser(User user) {
        Optional<List<MetersData>> rslOptional = Optional.empty();
        List<MetersData> rsl = store.get(user);
        if (rsl != null) {
            rslOptional = Optional.of(rsl);
        }
        return rslOptional;
    }

    public Optional<MetersData> getLastData(User user) {
        Optional<MetersData> rsl = Optional.empty();
        Optional<List<MetersData>> listOptional = findByUser(user);
        if (listOptional.isPresent()) {
            List<MetersData> list = listOptional.get();
            if (list.size() != 0) {
                rsl = Optional.of(list.get(list.size() - 1));
            }
        }
        return rsl;
    }

    public Optional<MetersData> getDataForSpecMonth(User user, LocalDateTime date) {
        Optional<MetersData> rsl = Optional.empty();
        Optional<List<MetersData>> listOptional = findByUser(user);
        if (listOptional.isPresent()) {
            List<MetersData> list = listOptional.get();
            if (list.size() != 0) {
                for (MetersData element : list) {
                    if (element.getDate().getMonth().equals(date.getMonth()) &&
                            element.getDate().getYear() == date.getYear()) {
                        rsl = Optional.of(element);
                    }
                }
            }
        }
        return rsl;
    }
}