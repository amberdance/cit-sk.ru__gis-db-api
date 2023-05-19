package ru.hard2code.gisdbapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.message.MessageService;
import ru.hard2code.gisdbapi.service.user.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = {"write", "read"})
class MessageControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api" + Route.MESSAGES;

    private final Message TEST_MESSAGE = new Message(null, "Label", "Answer",
            new User(null, "123456789", "username", "test@test.ru", Role.ADMIN, Collections.emptySet())
    );

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @AfterEach
    void cleanup() {
        messageService.deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    void testGetAll() throws Exception {
        var msg = List.of(messageService.createMessage(TEST_MESSAGE));

        mvc.perform(get(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(msg)));
    }

    @Test
    void testGetById() throws Exception {
        var msg = messageService.createMessage(TEST_MESSAGE);

        mvc.perform(get(API_PATH + "/{id}", msg.getId())
                        .contentType(CONTENT_TYPE).accept(CONTENT_TYPE))
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
        TEST_MESSAGE.getUser().setRole(Role.USER);

        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        messageService.createMessage(TEST_MESSAGE);

        TEST_MESSAGE.setQuestion("NEW_QUESTION");
        TEST_MESSAGE.setAnswer("NEW_ANSWER");

        mvc.perform(put(API_PATH + "/{id}", TEST_MESSAGE.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(TEST_MESSAGE.getQuestion()))
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

    @Test
    void testPartialUpdate() throws Exception {
        var message = messageService.createMessage(TEST_MESSAGE);

        message.setQuestion("updatedQuestion");
        message.setAnswer("updatedAnswer");

        mvc.perform(patch(API_PATH + "/{id}", message.getId())
                        .contentType(CONTENT_TYPE)
                        .content("{\"question\":\"" + message.getQuestion() + "\"," + "\"answer\":\"" + message.getAnswer() + "\"}")
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(message.getQuestion()))
                .andExpect(jsonPath("$.answer").value(message.getAnswer()))
                .andExpect(jsonPath("$.user").value(message.getUser()));
    }

}
