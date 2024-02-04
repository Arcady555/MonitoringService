package ru.parfenov.server.model;

public class User {
    private int id;
    private String login;
    private String password;
    private String history;

    public User() {
    }

    public User(int id, String login, String password, String history) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.history = history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}