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
import ru.hard2code.GisDatabaseApi.service.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest extends ControllerTestConfig {


    private final User TEST_USER = new User(0, "981283", "test@test.ru", "+79994446655", "username", "firstName", new UserType(UserType.Type.EMPLOYEE));

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        userService.create(TEST_USER);
        mvc.perform(get("/users/{id}", TEST_USER.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_USER)))
                .andReturn();
    }

    @Test
    @Transactional
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User("1", "userName1", "firstName1"),
                new User("2", "userName2", "firstName2"),
                new User("3", "userName3", "firstName3")
        );

        userService.saveAll(users);

        mvc.perform(get("/users").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        userService.create(TEST_USER);
        mvc.perform(delete("/users/{id}", TEST_USER.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(TEST_USER.getId()));
    }

    @Test
    @Transactional
    void shouldCreateUser() throws Exception {
        var userJson = OBJECT_MAPPER.writeValueAsString(TEST_USER);

        mvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(userJson)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = userService.create(TEST_USER);

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