package ru.parfenov.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String login;

    public UserDto() {
    }

    public UserDto(Integer id, String login) {
        this.id = id;
        this.login = login;
    }
}
