package ru.parfenov.server.serverinterface;

import ru.parfenov.server.consoleview.UserConsoleView;

import java.io.IOException;

public class Registration {
    private final UserConsoleView userConsoleView;

    public Registration(UserConsoleView userConsoleView) {
        this.userConsoleView = userConsoleView;
    }

    public void toReg() throws IOException {
        userConsoleView.inputForReg();
    }
}