package ru.hard2code.GisDatabaseApi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends ControllerTestConfig {


    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        var expectedUser = new User("chatId");

        userService.save(expectedUser);
        mvc.perform(get("/users/{id}", expectedUser.getId()).accept(HEADERS))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(expectedUser)));
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User("1"),
                new User("2"),
                new User("3")
        );

        userService.saveAll(users);

        mvc.perform(get("/users").accept(HEADERS))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        var user = new User("1");

        userService.save(user);
        mvc.perform(delete("/users/{id}", user.getId()).accept(HEADERS))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(user.getId()));
    }


}