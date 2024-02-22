package ru.parfenov.server.servlets.rest.login.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.servlets.rest.jacksonmodel.LoginMonthYear;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestSpecMonthOfUserServlet", urlPatterns = "/rest_spec-month-user")
public class RestSpecMonthOfUserServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationAdmin(response, login, out)) {
            if (validationEnter(request, response, login, out)) {
                Scanner scanner = new Scanner(request.getInputStream());
                String objJson = scanner.useDelimiter("\\A").next();
                scanner.close();
                ObjectMapper objectMapper = new ObjectMapper();
                LoginMonthYear loginMonthYear  = objectMapper.readValue(objJson, LoginMonthYear.class);
                String userLogin = loginMonthYear.getLogin();
                int month = loginMonthYear.getMonth();
                int year = loginMonthYear.getYear();
                List<PointValue> list = pointValueService.viewDataForSpecMonth(userLogin, month, year);
                printOut(list, response);
            }
        }
    }
}
