package ru.parfenov.server.servlets.rest.login.client;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
import ru.parfenov.server.service.PointValueService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestDataHistoryServlet", urlPatterns = "/rest_data-history")
public class RestDataHistoryServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationEnter(request, response, login, out)) {
            response.setContentType("text/html");
            List<PointValue> list = pointValueService.viewDataHistory(login);
            printOut(list, response);
        }
    }
}
