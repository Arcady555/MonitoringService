package ru.parfenov;

import ru.parfenov.client.ClientInterface;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("HELLO!!!" + System.lineSeparator() + "Welcome!");
        ClientInterface clientInterface = new ClientInterface();
        clientInterface.run();
    }
}