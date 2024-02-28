package ru.parfenov.server.servlets.start.login.admin.adminmethods;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
import ru.parfenov.server.service.PointValueService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static ru.parfenov.server.utility.Utility.printOut;

@WebServlet(name = "LastDataOfUserServlet", urlPatterns = "/last-data-of-user")
public class LastDataOfUserServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        request.getRequestDispatcher("/jsp/login/admin/methods/lastDataOfUser.html").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String login = request.getParameter("user_login");
        List<PointValue> list = pointValueService.viewLastData(login);
        printOut(list, response);
    }
}
