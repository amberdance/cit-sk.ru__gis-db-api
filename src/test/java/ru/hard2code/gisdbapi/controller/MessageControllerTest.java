package ru.hard2code.gisdbapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Message;
import ru.hard2code.gisdbapi.model.user.Role;
import ru.hard2code.gisdbapi.model.user.User;
import ru.hard2code.gisdbapi.service.message.MessageService;
import ru.hard2code.gisdbapi.service.user.UserRoleService;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
class MessageControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api/" + Route.MESSAGES;

    private final Role TEST_ROLE = new Role(Role.Type.CITIZEN.getValue());

    private final Message TEST_MESSAGE = new Message("Label",
            "Answer", new User("123456789", "userName",
            "test@test.ru", "+79994446655",
            TEST_ROLE,
            Collections.emptySet()));


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @AfterEach
    void cleanup() {
        messageService.deleteAll();
        userService.deleteAllUsers();
        userRoleService.deleteAllRoles();
    }

    @Test
    void testGetAll() throws Exception {
        var msg = List.of(messageService.createMessage(TEST_MESSAGE));

        mvc.perform(get(API_PATH).contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(msg)));
    }

    @Test
    void testGetById() throws Exception {
        var msg = messageService.createMessage(TEST_MESSAGE);

        mvc.perform(get(API_PATH + "/{id}", msg.getId()).contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(msg)));
    }

    @Test
    void testCreate() throws Exception {
        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateWithRoleId() throws Exception {
        var role =
                userRoleService.createRole(new Role(Role.Type.MUNICIPAL_EMPLOYEE.getValue()));

        TEST_MESSAGE.getUser().setRole(role);

        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        messageService.createMessage(TEST_MESSAGE);

        TEST_MESSAGE.setLabel("NEW_LABEL");
        TEST_MESSAGE.setAnswer("NEW_ANSWER");

        mvc.perform(put(API_PATH + "/{id}", TEST_MESSAGE.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value(TEST_MESSAGE.getLabel()))
                .andExpect(jsonPath("$.answer").value(TEST_MESSAGE.getAnswer()));
    }

    @Test
    void testDeleteById() throws Exception {
        messageService.createMessage(TEST_MESSAGE);

        mvc.perform(delete(API_PATH + "/{id}", TEST_MESSAGE.getId())
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> messageService.getMessageById(TEST_MESSAGE.getId()));
    }

}
