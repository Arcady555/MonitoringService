package ru.parfenov.server.servlets.start.reg;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;

import java.io.IOException;

@WebServlet(name = "RegServlet", urlPatterns = "/registration")
public class RegServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/reg/reg.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        userService.reg(login, password);
        // Mo
        response.sendRedirect("/Mo/login");
    }
}