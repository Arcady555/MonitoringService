package ru.parfenov.server.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.model.User;

import java.util.HashMap;
import java.util.Map;

class MemUserStoreTest {
    static MemUserStore memUserStore = new MemUserStore();
    static Map<String, User> userMap = new HashMap<>();
    static User user0;
    static User user1;
    static User user2;

    @BeforeAll
    static void setUp() {
        memUserStore = new MemUserStore();
        userMap = new HashMap<>();

        user0 = new User();
        user0.setLogin("admin");
        user0.setPassword("123");
        userMap.put("admin", user0);

        user1 = new User();
        user1.setLogin("Arcady");
        user1.setPassword("123");
        userMap.put("Arcady", user1);

        user2 = new User();
        user2.setLogin("NotArcady");
        user2.setPassword("123");
        userMap.put("NotArcady", user2);

        memUserStore.create("Arcady", "123");
        memUserStore.create("NotArcady", "123");
    }

    @Test
    void create() {
        Assertions.assertEquals(memUserStore.create("Arcady", "123").getLogin(), user1.getLogin());
        Assertions.assertEquals(memUserStore.create("Arcady", "123").getPassword(), user1.getPassword());
    }

    @Test
    void getAll() {
        Assertions.assertEquals(memUserStore.getAll().toArray().length, userMap.entrySet().toArray().length);
    }

    @Test
    void getByLogin() {
        Assertions.assertEquals(memUserStore.getByLogin("Arcady").getPassword(), "123");
    }
}