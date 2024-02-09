package ru.parfenov.server.serverinterface;

import ru.parfenov.server.consoleview.UserConsoleView;

public class Registration {
    private final UserConsoleView userConsoleView;

    public Registration(UserConsoleView userConsoleView) {
        this.userConsoleView = userConsoleView;
    }

    public void toReg() throws Exception {
        userConsoleView.inputForReg();
    }
}