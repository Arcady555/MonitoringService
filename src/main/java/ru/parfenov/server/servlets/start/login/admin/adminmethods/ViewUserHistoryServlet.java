package ru.parfenov.server.servlets.start.login.admin.adminmethods;

import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ViewUserHistoryServlet", urlPatterns = "/view-user-history")
public class ViewUserHistoryServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/admin/methods/userHistory.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        response.setContentType("text/html");
        String login = request.getParameter("user_login");
        String history = userService.viewUserHistory(login);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(history);
        out.flush();
    }
}
