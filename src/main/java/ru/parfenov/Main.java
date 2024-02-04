package ru.parfenov;

import ru.parfenov.client.ClientInterface;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("HELLO!!!" + System.lineSeparator() + "Welcome!");
        ClientInterface clientInterface = new ClientInterface();
        clientInterface.run();
    }
}