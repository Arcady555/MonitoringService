package ru.parfenov.server.servlets.rest.login.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestUserHistoryServlet", urlPatterns = "/rest_view-user-history")
public class RestUserHistoryServlet extends HttpServlet {
    private final UserService userService = new JdbcUserService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
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
                String history = userService.viewUserHistory(userLogin);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(history);
                out.flush();
            }
        }
    }
}
