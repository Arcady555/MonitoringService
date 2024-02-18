package ru.parfenov.server.controller.login;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.server.dto.UserDto;
import ru.parfenov.server.service.UserService;

import java.util.List;

@AllArgsConstructor
@RestController
public class AdminController {
  //  private final UserService userService;

    @GetMapping("/rest_all-users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return new ResponseEntity<>(
     //           userService.viewAllUsers(),
                HttpStatus.OK
        );
    }
}
