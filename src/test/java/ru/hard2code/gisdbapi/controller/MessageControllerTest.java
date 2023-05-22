package ru.hard2code.gisdbapi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.message.MessageService;
import ru.hard2code.gisdbapi.service.user.UserService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class MessageControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.MESSAGES;
    private final User TEST_USER = new User(null, "123456789", "username",
            "test@test.ru", Role.ADMIN, Collections.emptySet());
    private final Message TEST_MESSAGE =
            new Message(null, "Label", "Answer", TEST_USER);


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
        var message = List.of(messageService.createMessage(TEST_MESSAGE));

        mvc.perform(get(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(message)));
    }

    @Test
    void testGetById() throws Exception {
        var msg = messageService.createMessage(TEST_MESSAGE);

        mvc.perform(get(API_PATH + "/{id}", msg.getId())
                        .contentType(CONTENT_TYPE).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(msg)));
    }

    @Test
    void testCreateCascadeWithUser() throws Exception {
        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(TEST_MESSAGE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void whenPassedExistingIdInPOST_ThenMessageShouldBeCreatedInsteadUpdate()
            throws Exception {
        messageService.createMessage(TEST_MESSAGE);

        var anotherMessage = Message.builder()
                .id(TEST_MESSAGE.getId())
                .question("Another question")
                .user(TEST_USER)
                .build();

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(anotherMessage))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(not(TEST_MESSAGE.getId())));
    }

    @Test
    void whenPassedExistingUser_ThenOnlyMessageShouldCreatedNotCascade()
            throws Exception {
        var user = User.builder()
                .username("username")
                .chatId("9789110123")
                .email("some@email.com")
                .build();

        var userFromDb = userService.createUser(user);
        var message = new Message(null, "Question", "Answer", userFromDb);

        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(message))
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
                        .content(objectMapper.writeValueAsString(TEST_MESSAGE))
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
                        .content("{\"question\":\"" + message.getQuestion() + "\"," +
                                "\"answer\":\"" + message.getAnswer() + "\"}")
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(message.getQuestion()))
                .andExpect(jsonPath("$.answer").value(message.getAnswer()))
                .andExpect(jsonPath("$.user").value(message.getUser()));
    }

    @Test
    void testFindMessagesByUserId() throws Exception {
        var message = messageService.createMessage(TEST_MESSAGE);

        mvc.perform(get(API_PATH + "/user/{chatId}",
                        message.getUser().getChatId())
                        .contentType(CONTENT_TYPE).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(
                                Collections.singletonList(message))));
    }

}
