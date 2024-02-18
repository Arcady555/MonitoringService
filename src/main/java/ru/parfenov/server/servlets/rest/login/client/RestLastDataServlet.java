package ru.parfenov.server.servlets.rest.login.client;

import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.JdbcPointValueService;
import ru.parfenov.server.service.JdbcUserService;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestLastDataServlet", urlPatterns = "/rest_last-data")
public class RestLastDataServlet extends HttpServlet {
    private final PointValueService pointValueService = new JdbcPointValueService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationEnter(request, response, login, out)) {
            List<PointValue> list = pointValueService.viewLastData(login);
            printOut(list, response);
        }
    }
}
