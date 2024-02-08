package ru.parfenov.server.consoleview;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс служит для работы пользователя(не админа) со своими данными
 */
public class PointValueConsoleView {
    private final PointValueService pointValueService;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public PointValueConsoleView(PointValueService pointValueService) {
        this.pointValueService = pointValueService;
    }

    /**
     * Отсылка данных
     * Разрешена только в текущем месяце, один раз. Без редактирования.
     * Проверку выполняет метод validationOnceInMonth()
     * Допускается кроме 3х стандартных значений, ввести ещё свои какие-то показатели и заполнить их.
     *
     * @param login
     * @throws IOException
     */
    public void inputForSubmitData(String login) throws IOException {
        try {
            if (pointValueService.validationOnceInMonth(login)) {
                System.out.println("""
                        1 - Would you like to enter 3 points(heating, cool water, hot water) ?
                        2 - Would you like to enter more points ?
                        """);
                String answer1 = reader.readLine();
                List<PointValue> list = new ArrayList<>();
                if (answer1.equals("1")) {
                    printDataForSubmit(3, list);
                } else if (answer1.equals("2")) {
                    System.out.println("how many points will you create more?");

                    int answer2 = Integer.parseInt(reader.readLine());
                    if (answer2 > Utility.maxNumberOfPoints) {
                        System.out.println("It is too much!!! (Must be not over " + Utility.maxNumberOfPoints + ")");
                        inputForSubmitData(login);
                    } else {
                        printDataForSubmit(3 + answer2, list);
                    }
                } else {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    inputForSubmitData(login);
                }
                pointValueService.submitData(login, list);
                System.out.println("OK!" + System.lineSeparator());
            } else {
                System.out.println("This month data is already exist!!!" + System.lineSeparator());
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter correct!!!" + System.lineSeparator());
            inputForSubmitData(login);
        }
    }

    /**
     * Просмотр последних заполненных данных
     *
     * @param login
     */
    public void viewLastData(String login) {
        pointValueService.viewLastData(login);
    }

    /**
     * Просмотр данных за выбранный месяц
     *
     * @param login
     * @throws IOException
     */
    public void inputForViewDataForSpecMonth(String login) throws IOException {
        System.out.println("Which year are You interesting?");
        System.out.println("Please enter the number " + Utility.firstYear + "-" + LocalDateTime.now().getYear());
        int year = Integer.parseInt(reader.readLine());
        if (year > LocalDateTime.now().getYear() || year < Utility.firstYear) {
            System.out.println("Please enter correct" + System.lineSeparator());
            inputForViewDataForSpecMonth(login);
        } else {
            System.out.println("""
                    Which month are You interesting?
                    (Please enter the number 1-12)
                    """);
            try {
                int month = Integer.parseInt(reader.readLine());
                if (month > 12 || month < 1) {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    inputForViewDataForSpecMonth(login);
                } else {
                    pointValueService.viewDataForSpecMonth(login, month, year);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter correct!!!" + System.lineSeparator());
                inputForViewDataForSpecMonth(login);
            }
        }
    }

    /**
     * Просмотр всех своих заполненных данных
     *
     * @param login
     */
    public void viewDataHistory(String login) {
        pointValueService.viewDataHistory(login);
    }

    public void out(String login) {
        pointValueService.toOut(login);
    }

    /**
     * Помогает сформировать данные для отправки в хранилище
     *
     * @param pointNumber
     * @param list
     * @throws IOException
     */
    private void printDataForSubmit(int pointNumber, List<PointValue> list) throws IOException {
        List<String> pointList = new ArrayList<>();
        pointList.add("heating");
        pointList.add("cool water");
        pointList.add("hot water");
        if (pointNumber > 3) {
            for (int i = 4; i < pointNumber + 1; i++) {
                System.out.println("Enter name of " + i + "  point");
                String answer = reader.readLine();
                pointList.add(answer);
            }
        }
        for (String s : pointList) {
            System.out.println("Enter " + s + " value");
            int value = Integer.parseInt(reader.readLine());
            list.add(new PointValue(s, value));
        }
    }
}
