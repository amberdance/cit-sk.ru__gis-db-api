package ru.hard2code.gisdbapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;
import ru.hard2code.gisdbapi.domain.entity.Role;

import java.io.Serializable;

/**
 * DTO for {@link ru.hard2code.gisdbapi.domain.entity.User}
 */
@Value
@Schema(name = "User")
public class UserDto implements Serializable {

    Long id;

    @NotNull
    @Pattern(regexp = "^\\d{9,12}$")
    String chatId;

    @NotNull
    String username;

    @Email
    String email;

    Role role;

}
