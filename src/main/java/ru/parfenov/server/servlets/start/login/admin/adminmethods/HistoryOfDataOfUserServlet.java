package ru.parfenov.server.servlets.start.login.admin.adminmethods;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.PointValueService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.parfenov.server.utility.Utility.printOut;

@WebServlet(name = "HistoryOfDataOfUserServlet", urlPatterns = "/history-data-user")
public class HistoryOfDataOfUserServlet extends HttpServlet {
    private final PointValueService pointValueService = new JdbcPointValueService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/admin/methods/historyOfDataOfUser.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String login = request.getParameter("user_login");
        List<PointValue> list = pointValueService.viewDataHistory(login);
        printOut(list, response);
    }
}