package ru.hard2code.gisdbapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link ru.hard2code.gisdbapi.domain.entity.User}
 */
@Value
@Schema(name = "User")
public class UserDto implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^\\d{9,12}$")
    private String chatId;

    @NotNull
    private String username;

    @Email
    private String email;

}
