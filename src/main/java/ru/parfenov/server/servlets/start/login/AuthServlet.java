package ru.parfenov.server.servlets.start.login;

import jakarta.servlet.http.HttpSession;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AuthServlet", urlPatterns = "/login")
public class AuthServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/login.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        User user = userService.getByLogin(login);
        if (user.getPassword().equals(password)) {
            session.setAttribute("login", login);
            if (user.getLogin().equals("admin")) {
                //Mo
                response.sendRedirect("/ms/admin");
            } else {
                //Mo
                response.sendRedirect("/ms/client");
            }
        } else {
            request.setAttribute("error", "invalid login");
            response.sendRedirect(request.getParameter("/login"));
        }
        out.close();
    }
}
