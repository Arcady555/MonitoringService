package ru.parfenov.server.servlets.rest.login.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.PointValueService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestLastDataOfUserServlet", urlPatterns = "/rest_last-data-of-user")
public class RestLastDataOfUserServlet extends HttpServlet {
    private final PointValueService pointValueService = new JdbcPointValueService();

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
                List<PointValue> list = pointValueService.viewLastData(userLogin);
                printOut(list, response);
            }
        }
    }
}
