package ru.parfenov.server.servlets.rest.login.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
import ru.parfenov.server.service.PointValueService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestHistoryOfDataOfUserServlet", urlPatterns = "/rest_history-data-user")
public class RestHistoryOfDataOfUserServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationAdmin(response, login, out)) {
            if (validationEnter(request, response, login, out)) {
                Scanner scanner = new Scanner(request.getInputStream());
                String userJson = scanner.useDelimiter("\\A").next();
                scanner.close();
                ObjectMapper objectMapper = new ObjectMapper();
                UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
                String userLogin = userDto.getLogin();
                List<PointValue> list = pointValueService.viewDataHistory(userLogin);
                printOut(list, response);
            }
        }
    }
}