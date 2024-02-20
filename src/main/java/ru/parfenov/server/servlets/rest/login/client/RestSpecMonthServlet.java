package ru.parfenov.server.servlets.rest.login.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.servlets.rest.jacksonmodel.MonthYear;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestSpecMonthServlet", urlPatterns = "/rest_spec-month")
public class RestSpecMonthServlet extends HttpServlet {
    private final PointValueService pointValueService = new JdbcPointValueService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationEnter(request, response, login, out)) {
            Scanner scanner = new Scanner(request.getInputStream());
            String userJson = scanner.useDelimiter("\\A").next();
            scanner.close();
            ObjectMapper objectMapper = new ObjectMapper();
            MonthYear monthYear = objectMapper.readValue(userJson, MonthYear.class);
            int month = monthYear.getMonth();
            int year = monthYear.getYear();
            List<PointValue> list = pointValueService.viewDataForSpecMonth(login, month, year);
            printOut(list, response);
        }
    }
}
