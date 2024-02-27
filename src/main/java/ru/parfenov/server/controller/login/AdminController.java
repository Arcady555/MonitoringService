package ru.parfenov.server.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.service.PointValueService;
import ru.parfenov.server.service.UserService;

import java.util.List;

@RestController
public class AdminController {
    private final UserService userService;
    private final PointValueService pointValueService;

    @Autowired
    public AdminController(UserService userService, PointValueService pointValueService) {
        this.userService = userService;
        this.pointValueService = pointValueService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        try {
            userService.viewAllUsers();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/history-data-user")
    public ResponseEntity<List<PointValueDto>> historyOfDataOfUser(@RequestBody String login) {
        try {
            return new ResponseEntity<>(
                    pointValueService.viewDataHistory(login),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/last-data-of-user")
    public ResponseEntity<List<PointValueDto>> lastDataOfUser(@RequestBody String login) {
        try {
            return new ResponseEntity<>(
                    pointValueService.viewLastData(login),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/spec-month-user")
    public ResponseEntity<List<PointValueDto>> specMonthOfUser(
            @RequestBody String login, @RequestParam int month, @RequestParam int year) {
        try {
            return new ResponseEntity<>(
                    pointValueService.viewDataForSpecMonth(login, month, year),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/user-history")
    public ResponseEntity<String> userHistory(@RequestBody String login) {
        try {
            return new ResponseEntity<>(
                    userService.viewUserHistory(login),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
