package ru.hard2code.gisdbapi.domain.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.hard2code.gisdbapi.domain.dto.UserDto;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.domain.entity.User.UserBuilder;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-05-18T20:27:12+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.chatId( userDto.getChatId() );
        user.username( userDto.getUsername() );
        user.email( userDto.getEmail() );
        user.role( userDto.getRole() );

        return user.build();
    }

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String chatId = null;
        String username = null;
        String email = null;
        Role role = null;

        id = user.getId();
        chatId = user.getChatId();
        username = user.getUsername();
        email = user.getEmail();
        role = user.getRole();

        UserDto userDto = new UserDto( id, chatId, username, email, role );

        return userDto;
    }

    @Override
    public User partialUpdate(UserDto userDto, User user) {
        if ( userDto == null ) {
            return null;
        }

        if ( userDto.getId() != null ) {
            user.setId( userDto.getId() );
        }
        if ( userDto.getChatId() != null ) {
            user.setChatId( userDto.getChatId() );
        }
        if ( userDto.getUsername() != null ) {
            user.setUsername( userDto.getUsername() );
        }
        if ( userDto.getEmail() != null ) {
            user.setEmail( userDto.getEmail() );
        }
        if ( userDto.getRole() != null ) {
            user.setRole( userDto.getRole() );
        }

        return user;
    }
}
