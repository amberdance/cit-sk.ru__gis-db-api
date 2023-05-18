package ru.hard2code.gisdbapi.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.hard2code.gisdbapi.dto.UserDto;
import ru.hard2code.gisdbapi.model.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-18T17:34:49+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto map(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setChatId( user.getChatId() );
        userDto.setEmail( user.getEmail() );
        userDto.setRole( user.getRole() );

        return userDto;
    }

    @Override
    public User map(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User user = new User();

        user.setChatId( userDto.getChatId() );
        user.setEmail( userDto.getEmail() );
        user.setRole( userDto.getRole() );

        return user;
    }
}
