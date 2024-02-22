package ru.parfenov.server.servlets.rest.login.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestAllUserServlet", urlPatterns = "/rest_all-users")
public class RestAllUserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationAdmin(response, login, out)) {
            if (validationEnter(request, response, login, out)) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<UserDto> list = userService.viewAllUsers();
                String obElement = objectMapper.writeValueAsString(list);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(obElement);
                out.flush();
            }
        }
    }
}
