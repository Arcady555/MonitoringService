package ru.parfenov.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Integer id;
    private String login;
    private String password;
    private String history;

    public User(int id, String login, String password, String history) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.history = history;
    }

    public User(int id, String login, String history) {
        this.id = id;
        this.login = login;
        this.history = history;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User() {
    }
}