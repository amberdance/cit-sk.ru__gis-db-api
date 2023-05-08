package ru.hard2code.gisdbapi.controller;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.model.UserType;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends ControllerTestConfig {

    private final User TEST_USER = new User("123456789", "test@test.ru", "+79994446655",
            "username", "firstName", new UserType(UserType.Type.GOVERNMENT_EMPLOYEE));

    @Autowired
    private UserService userService;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from users");
    }

    @Test
    void shouldReturnUserById() throws Exception {
        userService.createUser(TEST_USER);
        mvc.perform(get("/users/{id}", TEST_USER.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_USER)));
    }

    @Test
    @Transactional
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User("123123123", "test@test1.ru", "+79994446651",
                        "username1", "firstName1", new UserType(UserType.Type.GOVERNMENT_EMPLOYEE)),
                new User("432432432", "test@test2.ru", "+79994446652",
                        "username2", "firstName2", new UserType(UserType.Type.MUNICIPAL_EMPLOYEE))
        );

        userService.createUser(users.get(0));
        userService.createUser(users.get(1));

        mvc.perform(get("/users").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        userService.createUser(TEST_USER);
        mvc.perform(delete("/users/{id}", TEST_USER.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(TEST_USER.getId()));
    }

    @Test
    void shouldCreateUser() throws Exception {
        var userJson = OBJECT_MAPPER.writeValueAsString(TEST_USER);

        mvc.perform(post("/users")
                        .contentType(CONTENT_TYPE)
                        .content(userJson)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = userService.createUser(TEST_USER);

        user.setChatId("999999999");
        user.setUserType(new UserType(UserType.Type.GOVERNMENT_EMPLOYEE));
        user.setUserName("newUserName");
        user.setPhone("+79994443399");
        user.setEmail("newemail@test.com");
        user.setFirstName("firstNameNew");

        mvc.perform(put("/users/{id}", user.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(user.getChatId()))
                .andExpect(jsonPath("$.userType.type").value(user.getUserType().getType().getValue()))
                .andReturn();
    }

}