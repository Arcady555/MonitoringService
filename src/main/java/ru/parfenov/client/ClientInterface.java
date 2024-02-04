package ru.parfenov.client;

import ru.parfenov.server.ServerClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс отвечает за начало программы,
 * выводит текст - меню для пользователя
 */

public class ClientInterface {
    /**
     * Запуск сервера
     */
    ServerClass server = new ServerClass();
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    public ClientInterface() throws Exception {
    }

    public void run() throws IOException {
        System.out.println("""
                Please enter:
                1 - register
                or
                2 - enter login
                or
                3 - exit
                """);
        String enter = r.readLine();
        switch (enter) {
            case "1":
                server.reg();
                break;
            case "2":
                server.auth();
                break;
            case "3":
                return;
            default:
                System.out.println("Please enter correct" + System.lineSeparator());
        }
        run();
    }
}