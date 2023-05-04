package ru.hard2code.GisDatabaseApi.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.model.UserType;
import ru.hard2code.GisDatabaseApi.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest extends ControllerTestConfig {


    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        var expectedUser = new User("chatId");

        userService.create(expectedUser);
        mvc.perform(get("/users/{id}", expectedUser.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(expectedUser)))
                .andReturn();
    }

    @Test
    @Transactional
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User("1"),
                new User("2"),
                new User("3")
        );

        userService.saveAll(users);

        mvc.perform(get("/users").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        var user = new User("1");

        userService.create(user);
        mvc.perform(delete("/users/{id}", user.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    @Transactional
    void shouldCreateUser() throws Exception {
        var userJson = OBJECT_MAPPER.writeValueAsString(new User("981283", new UserType(UserType.Type.EMPLOYEE)));

        mvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(userJson)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = userService.create(new User("OLD_CHAT_ID", new UserType(UserType.Type.CITIZEN)));

        user.setChatId("NEW_CHAT_ID");
        user.setUserType(new UserType(UserType.Type.EMPLOYEE));

        mvc.perform(put("/users/{id}", user.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(user.getChatId()))
                .andExpect(jsonPath("$.userType.type").value(user.getUserType().getType().toString()))
                .andReturn();
    }


}