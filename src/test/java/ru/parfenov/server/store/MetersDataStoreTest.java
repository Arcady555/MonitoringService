package ru.parfenov.server.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.model.MetersData;
import ru.parfenov.server.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetersDataStoreTest {
    static MetersDataStore store;
    static MetersData data1;
    static MetersData data2;
    static User user1;
    static User user2;

    @BeforeAll
    static void setUp() {
        store = new MetersDataStore();

        data1 = new MetersData();
        user1 = new User();
        user1.setLogin("Arcady");
        user1.setPassword("123");
        store.createDataList(user1);
        store.createData(user1, data1);

        data2 = new MetersData();
        user2 = new User();
        user2.setLogin("notArcady");
        user2.setPassword("123");
        store.createDataList(user2);
        store.createData(user2, data2);
    }

    @Test
    void findByUser() {
        Assertions.assertEquals(store.findByUser(user1).get(), List.of(data1));
        Assertions.assertEquals(store.findByUser(user2).get(), List.of(data2));
    }

    @Test
    void getLastData() {
        Assertions.assertEquals(store.getLastData(user1).get(), data1);
        Assertions.assertEquals(store.getLastData(user2).get(), data2);
    }
}