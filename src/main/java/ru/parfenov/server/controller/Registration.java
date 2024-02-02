package ru.parfenov.server.controller;

import ru.parfenov.server.service.UserService;

import java.io.IOException;

public class Registration {
    private final UserService service;

    public Registration(UserService service) {
        this.service = service;
    }

    public void toReg() throws IOException {
        service.reg();
    }
}