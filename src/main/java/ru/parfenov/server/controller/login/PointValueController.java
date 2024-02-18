package ru.parfenov.server.controller.login;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.model.PointValue;
import ru.parfenov.server.service.PointValueService;

import javax.servlet.http.HttpSession;
import java.util.List;

@AllArgsConstructor
@RestController
public class PointValueController {
   // private final PointValueService pointValueService;

    @PostMapping("/rest_submit-data")
    public ResponseEntity<Void> submitData(@RequestBody List<PointValue> list, HttpSession session) {
        try {
            String login = session.getAttribute("login").toString(); //////////////////////
        //    pointValueService.submitData(login, list);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

}
