package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = {"write", "read"})
class UserControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api" + Route.USERS;
    private final User TEST_USER = new User(null, "123456789", "username", "test@test.ru", Role.USER,
            Collections.emptySet()
    );

    @Autowired
    private UserService userService;


    @AfterEach
    void cleanup() {
        userService.deleteAllUsers();
    }

    @Test
    void testFindById() throws Exception {
        userService.createUser(TEST_USER);
        mvc.perform(get(API_PATH + "/{id}", TEST_USER.getId())
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_USER)));
    }

    @Test
    void testFindAll() throws Exception {
        var users = List.of(
                new User(null, "123123123", "username1", "test@test1.ru", Role.ADMIN, Collections.emptySet()),
                new User(null, "432432432", "username2", "test@test2.ru", Role.USER, Collections.emptySet())
        );

        users.forEach(userService::createUser);

        mvc.perform(get(API_PATH)
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(users)));
    }

    @Test
    void testDeleteById() throws Exception {
        userService.createUser(TEST_USER);

        mvc.perform(delete(API_PATH + "/{id}", TEST_USER.getId())
                   .accept(CONTENT_TYPE))
           .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserById(TEST_USER.getId())
        );
    }

    @Test
    void testCreate() throws Exception {
        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                                  .content(OBJECT_MAPPER.writeValueAsString(TEST_USER))
                                  .accept(CONTENT_TYPE))
           .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        var user = userService.createUser(TEST_USER);

        user = user.toBuilder()
                .chatId("999999999")
                .role(Role.ADMIN)
                .username("newUserName")
                .email("newemail@test.com")
                .build();

        mvc.perform(put(API_PATH + "/{id}", user.getId())
                   .contentType(CONTENT_TYPE)
                   .content(OBJECT_MAPPER.writeValueAsString(user))
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.chatId").value(user.getChatId()))
           .andExpect(jsonPath("$.role").value(user.getRole().toString()))
           .andExpect(jsonPath("$.username").value(user.getUsername()))
           .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testValidation() throws Exception {
        var wrongUser = new User(null, "chatId", "username", "email", Role.ADMIN, Collections.emptySet());

        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                                  .content(OBJECT_MAPPER.writeValueAsString(wrongUser))
                                  .accept(CONTENT_TYPE))
           .andExpect(status().isBadRequest());
    }

}
