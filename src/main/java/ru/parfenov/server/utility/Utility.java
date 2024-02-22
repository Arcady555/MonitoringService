package ru.parfenov.server.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.dto.PointValueToDtoMapper;
import ru.parfenov.server.dto.PointValueToDtoMapperImpl;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.UserServiceImpl;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.store.UserStore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;


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

    public static List<PointValueDto> getListDto(List<PointValue> list) {
        PointValueToDtoMapper pointValueToDtoMapper = new PointValueToDtoMapperImpl();
        List<PointValueDto> listDto = new ArrayList<>();
        for (PointValue pointValue : list) {
            PointValueDto pointValueDto = pointValueToDtoMapper.toPointValueDto(pointValue);
            listDto.add(pointValueDto);
        }
        return listDto;
    }

    public static List<PointValue> getList(List<PointValueDto> listDto, int userId) {
        PointValueToDtoMapper pointValueToDtoMapper = new PointValueToDtoMapperImpl();
        List<PointValue> list = new ArrayList<>();
        for (PointValueDto pointValueDto : listDto) {
            PointValue pointValue = pointValueToDtoMapper.toPointValue(pointValueDto);
            pointValue.setUserId(userId);
            list.add(pointValue);
        }
        return list;
    }

    public static boolean validationAdmin(HttpServletResponse response, String login, PrintWriter out) {
        if (login.equals("admin")) {
            return true;
        }
        response.setStatus(400);
        out.print("Not permission!");
        out.flush();
        return false;
    }

    public static boolean validationEnter(HttpServletRequest request,
                                          HttpServletResponse response,
                                          String login,
                                          PrintWriter out) {
        UserService userService = new UserServiceImpl();
        String password = getUserPassword(request);
        if (userService.getByLogin(login) != null
                && password.equals(userService.getByLogin(login).getPassword())) {
            return true;
        }
        response.setStatus(400);
        out.print("Login and password not correct!");
        out.flush();
        return false;
    }

    public static String getUserLogin(HttpServletRequest request) {
        String loginAndPassword = getLoginAndPassword(request);
        return loginAndPassword != null ? loginAndPassword.split(" ")[0] : "";
    }

    public static String getUserPassword(HttpServletRequest request) {
        String loginAndPassword = getLoginAndPassword(request);
        return loginAndPassword != null ? loginAndPassword.split(" ")[1] : "";
    }

    private static String getLoginAndPassword(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();
                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        byte[] decoded = Base64.getDecoder().decode(st.nextToken());
                        String credentials = new String(decoded, StandardCharsets.UTF_8);
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String login = credentials.substring(0, p).trim();
                            String password = credentials.substring(p + 1).trim();
                            return (login + " " + password);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }
}