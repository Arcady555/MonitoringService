package ru.parfenov.server.servlets.start.login.admin.adminmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAllUserServlet", urlPatterns = "/all-users")
public class GetAllUserServlet extends HttpServlet {
    private final UserService userService = new JdbcUserService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> list = userService.viewAllUsers();
        String obElement = objectMapper.writeValueAsString(list);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(obElement);
        out.flush();
    }
}
