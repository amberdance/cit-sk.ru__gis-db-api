package ru.hard2code.gisdbapi.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link ru.hard2code.gisdbapi.domain.entity.Message}
 */
@Value
@Schema(name = "Message")
public class MessageDto implements Serializable {

    Long id;

    @NotNull
    String question;

    String answer;

    @NotNull
    UserDto user;
}
