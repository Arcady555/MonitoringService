package ru.parfenov.server.servlets.start.login.client.clientmethods;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
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

@WebServlet(name = "SpecMonthServlet", urlPatterns = "/spec-month")
public class ViewDataForSpecMonthServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/client/methods/specMonth.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("login");
        int month = Integer.parseInt(request.getParameter("month"));
        int year = Integer.parseInt(request.getParameter("year"));
        List<PointValue> list = pointValueService.viewDataForSpecMonth(login, month, year);
        printOut(list, response);
    }
}
