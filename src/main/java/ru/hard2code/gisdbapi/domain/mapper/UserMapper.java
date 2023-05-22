package ru.hard2code.gisdbapi.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.hard2code.gisdbapi.domain.dto.UserDto;
import ru.hard2code.gisdbapi.domain.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel =
        "spring")

public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
    
}
