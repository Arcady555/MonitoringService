package ru.parfenov.server.servlets.start.login.admin.adminmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAllUserServlet", urlPatterns = "/all-users")
public class GetAllUserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<UserDto> list = userService.viewAllUsers();
        String obElement = objectMapper.writeValueAsString(list);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(obElement);
        out.flush();
    }
}
