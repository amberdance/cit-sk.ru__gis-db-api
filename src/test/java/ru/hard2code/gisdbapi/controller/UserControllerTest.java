package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.User;
import ru.hard2code.gisdbapi.model.UserType;
import ru.hard2code.gisdbapi.service.user.UserService;
import ru.hard2code.gisdbapi.service.userType.UserTypeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends ControllerTestConfig {

    private final UserType CITIZEN = new UserType(UserType.Type.CITIZEN.getValue());
    private final UserType EMPLOYEE = new UserType(UserType.Type.GOVERNMENT_EMPLOYEE.getValue());
    private final User TEST_USER = new User("123456789", "test@test.ru", "+79994446655",
            "username", "firstName", CITIZEN);

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;

    @BeforeEach
    void beforeEach() {
        userTypeService.createUserType(CITIZEN);
        userTypeService.createUserType(EMPLOYEE);
    }

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from users");
        jdbcTemplate.execute("delete from user_types");
    }

    @Test
    void shouldReturnUserById() throws Exception {
        userService.createUser(TEST_USER);
        mvc.perform(get("/users/{id}", TEST_USER.getId())
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_USER)));
    }

    @Test
    void shouldReturnListOfUsers() throws Exception {
        var users = List.of(
                new User("123123123", "test@test1.ru", "+79994446651",
                        "username1", "firstName1", CITIZEN),
                new User("432432432", "test@test2.ru", "+79994446652",
                        "username2", "firstName2", EMPLOYEE)
        );

        userService.createUser(users.get(0));
        userService.createUser(users.get(1));

        mvc.perform(get("/users")
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void shouldDeleteUserById() throws Exception {
        userService.createUser(TEST_USER);

        mvc.perform(delete("/users/{id}", TEST_USER.getId())
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(TEST_USER.getId()));
    }

    @Test
    void shouldCreateUser() throws Exception {
        var userJson = OBJECT_MAPPER.writeValueAsString(TEST_USER);

        mvc.perform(post("/users")
                        .with(user(TEST_USER_ROLE))
                        .contentType(CONTENT_TYPE)
                        .content(userJson)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var user = userService.createUser(TEST_USER);

        user.setChatId("999999999");
        user.setUserType(EMPLOYEE);
        user.setUserName("newUserName");
        user.setPhone("+79994443399");
        user.setEmail("newemail@test.com");
        user.setFirstName("firstNameNew");

        mvc.perform(put("/users/{id}", user.getId())
                        .with(user(TEST_USER_ROLE))
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(user))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(user.getChatId()))
                .andExpect(jsonPath("$.userType.name").value(user.getUserType().getName()))
                .andReturn();
    }

}