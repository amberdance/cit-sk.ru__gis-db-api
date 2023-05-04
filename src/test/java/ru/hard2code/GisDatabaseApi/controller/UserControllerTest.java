package ru.hard2code.GisDatabaseApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.model.UserType;
import ru.hard2code.GisDatabaseApi.service.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        var expectedUser = new User(null, "chatId", new UserType(null, UserType.Type.CITIZEN));

        userService.save(expectedUser);

        mvc.perform(get("/users/{id}", expectedUser.getId()).accept("application/json; charset=utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User(null, "1", new UserType(null, UserType.Type.CITIZEN)),
                new User(null, "2", new UserType(null, UserType.Type.EMPLOYEE)),
                new User(null, "3", new UserType(null, UserType.Type.CITIZEN))
        );

        userService.saveAll(users);

        mvc.perform(get("/users").accept("application/json; charset=utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(users)));
    }
}