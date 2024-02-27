package ru.parfenov.server.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.server.dto.PointValueDto;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueService;

//import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PointValueController {
    private final PointValueService pointValueService;

    @Autowired
    public PointValueController(PointValueService pointValueService) {
        this.pointValueService = pointValueService;
    }

    @PostMapping("/submit-data")
    public ResponseEntity<Void> submitData(@RequestBody List<PointValue> list) { //}, HttpSession session) {
        try {
         //   String login = session.getAttribute("login").toString(); /////////////////
         //   pointValueService.submitData(login, list);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/spec-month")
    public ResponseEntity<List<PointValueDto>> specMonth(@RequestParam("username") String login,
                                                         @RequestParam("password") String password,
                                                         @RequestParam("month") int month,
                                                         @RequestParam("year") int year) {
        try {
         //   validation(password);
            return new ResponseEntity<>(pointValueService.viewDataForSpecMonth(login, month, year),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/last-data")
    public ResponseEntity<List<PointValueDto>> lastData(@RequestParam("username") String login,
                                                         @RequestParam("password") String password) {
        try {
            return new ResponseEntity<>(pointValueService.viewLastData(login),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/data-history")
    public ResponseEntity<List<PointValueDto>> specMonth(@RequestParam("username") String login,
                                                         @RequestParam("password") String password) {
        try {
            return new ResponseEntity<>(pointValueService.viewDataHistory(login),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
