package ru.hard2code.gisdbapi.domain.dto;

import lombok.Data;
import ru.hard2code.gisdbapi.domain.entity.Role;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.hard2code.gisdbapi.domain.entity.User} entity
 */
@Data
public class UserDto implements Serializable {

    private final Long id;
    private final String chatId;
    private final String username;
    private final String email;
    private final Role role;
}
