package ru.parfenov.server.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.dto.PointValueToDtoMapper;
import ru.parfenov.server.dto.PointValueToDtoMapperImpl;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;
import ru.parfenov.server.store.UserStore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    public static int maxNumberOfPoints = 10;
    public static String exitWord = "exit";
    public static int firstYear = 2015;

    private Utility() {
    }

    public static void fixTime(UserStore userStore, String login, String nameOfMethod) {
        String history = (LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                + " "
                + nameOfMethod
                + System.lineSeparator());
        User user = userStore.getByLogin(login).get();
        String newHistory = user.getHistory() + history;
        userStore.insertUserHistory(user, newHistory);
    }

    public static void printOut(List<PointValue> list, HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        List<PointValueDto> listDto = getListDto(list);
        String obElement = objectMapper.writeValueAsString(listDto);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(obElement);
        out.flush();
    }

    private static List<PointValueDto> getListDto(List<PointValue> list) {
        PointValueToDtoMapper pointValueToDtoMapper = new PointValueToDtoMapperImpl();
        List<PointValueDto> listDto = new ArrayList<>();
        for (PointValue pointValue : list) {
            PointValueDto pointValueDto = pointValueToDtoMapper.toPointValueDto(pointValue);
            listDto.add(pointValueDto);
        }
        return listDto;
    }
}
