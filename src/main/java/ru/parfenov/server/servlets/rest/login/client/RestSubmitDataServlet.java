package ru.parfenov.server.servlets.rest.login.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueServiceImpl;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static ru.parfenov.server.utility.Utility.*;

@WebServlet(name = "RestSubmitDataServlet", urlPatterns = "/rest_submit-data")
public class RestSubmitDataServlet extends HttpServlet {
    private final PointValueService pointValueService = new PointValueServiceImpl();
    private final UserService userService = new UserServiceImpl();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = getUserLogin(request);
        PrintWriter out = response.getWriter();
        if (validationEnter(request, response, login, out)) {
            Scanner scanner = new Scanner(request.getInputStream());
            String listJson = scanner.useDelimiter("\\A").next();
            scanner.close();
            ObjectMapper objectMapper = new ObjectMapper();
            List<PointValueDto> listDto = objectMapper.readValue(listJson, new TypeReference<>() {
            });
            response.setContentType("text/html");
            if (pointValueService.validationOnceInMonth(login)) {
                int userId = userService.getByLogin(login).getId();
                List<PointValue> list = getList(listDto, userId);
                pointValueService.submitData(login, list);
                response.setStatus(200);
                out.print("Ok!");
            } else {
                response.setStatus(400);
                out.print("This month data is already exist!");
            }
        }
        out.flush();
    }
}
