package ru.parfenov.server.servlets.start.login;

import ru.parfenov.server.model.User;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AuthServlet", urlPatterns = "/login")
public class AuthServlet extends HttpServlet {
    private final UserService userService = new JdbcUserService();

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
                response.sendRedirect("/Mo/admin");
            } else {
                //Mo
                response.sendRedirect("/Mo/client");
            }
        } else {
            request.setAttribute("error", "invalid login");
            response.sendRedirect(request.getParameter("/login"));
        }
        out.close();
    }
}
