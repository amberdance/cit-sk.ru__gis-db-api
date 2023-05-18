package ru.hard2code.gisdbapi.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.hard2code.gisdbapi.dto.UserDto;
import ru.hard2code.gisdbapi.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(User user);

    @InheritInverseConfiguration
    User map(UserDto userDto);

}
