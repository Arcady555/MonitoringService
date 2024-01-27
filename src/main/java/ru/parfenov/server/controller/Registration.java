package ru.parfenov.server.controller;

import ru.parfenov.server.service.OperationService;

import java.io.IOException;

public class Registration {
    private final OperationService service;

    public Registration(OperationService service) {
        this.service = service;
    }

    public void toReg() throws IOException {
        service.reg();
    }
}