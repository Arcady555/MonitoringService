package ru.parfenov.server.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.parfenov.server.model.User;

@Mapper
public interface UserToDtoMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDto toUserDto(User source);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    User toUser(UserDto destination);
}