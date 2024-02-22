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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SubmitDataServlet", urlPatterns = "/submit-data")
public class SubmitDataServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/client/methods/submitData.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        String login = (String) session.getAttribute("login");
        List<PointValue> list = new ArrayList<>();
        PointValue pointValue1 = new PointValue(
                "heating", Integer.parseInt(request.getParameter("heating"))
        );
        PointValue pointValue2 = new PointValue(
                "coolWater", Integer.parseInt(request.getParameter("coolWater"))
        );
        PointValue pointValue3 = new PointValue(
                "hotWater", Integer.parseInt(request.getParameter("hotWater"))
        );
        list.add(pointValue1);
        list.add(pointValue2);
        list.add(pointValue3);
        pointValueService.submitData(login, list);
        response.sendRedirect("/Mo/client");
    }
}
