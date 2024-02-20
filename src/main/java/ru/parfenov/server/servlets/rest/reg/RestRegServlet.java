package ru.parfenov.server.servlets.rest.reg;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet(name = "RestRegServlet", urlPatterns = "/rest_reg")
public class RestRegServlet extends HttpServlet {
    private final UserService userService = new JdbcUserService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Scanner scanner = new Scanner(request.getInputStream());
        String userJson = scanner.useDelimiter("\\A").next();
        scanner.close();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);
        if (userService.getByLogin(user.getLogin()) == null) {
            userService.reg(user.getLogin(), user.getPassword());
            response.setStatus(200);
        } else {
            response.setStatus(400);
            PrintWriter out = response.getWriter();
            out.print("User is already exist!");
            out.flush();
        }
    }
}