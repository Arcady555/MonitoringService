package ru.parfenov.server.servlets.start.login.client.clientmethods;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.PointValueService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static ru.parfenov.server.utility.Utility.printOut;

@WebServlet(name = "ViewLastDataServlet", urlPatterns = "/view-last-data")
public class ViewLastDataServlet extends HttpServlet {
    private final PointValueService pointValueService = new JdbcPointValueService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("login");
        List<PointValue> list = pointValueService.viewLastData(login);
        printOut(list, response);
    }
}
