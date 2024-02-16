package ru.parfenov.server.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.parfenov.server.model.PointValue;

@Mapper
public interface PointValueToDtoMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "date", source = "date",
            dateFormat = "dd-MM-yyyy HH:mm")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "value", source = "value")
    PointValueDto toPointValueDto(PointValue source);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "date", source = "date",
            dateFormat = "dd-MM-yyyy HH:mm:ss")
    @Mapping(target = "point", source = "point")
    @Mapping(target = "value", source = "value")
    PointValue toPointValue(PointValueDto destination);
}
