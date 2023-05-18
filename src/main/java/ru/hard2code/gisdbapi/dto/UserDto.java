package ru.hard2code.gisdbapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hard2code.gisdbapi.model.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

    private Long id;
    private String chatId;
    private String username;
    private String email;
    private Role role;

}
