package ru.hard2code.gisdbapi.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Message;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.message.MessageService;
import ru.hard2code.gisdbapi.service.user.UserService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.Collections;
import java.util.Random;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class MessageControllerTest extends AbstractTestContainersControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.MESSAGES;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void setup() {
        startContainer();
    }


    private Message createMessage() {
        var randomString = RandomStringUtils.random(32);
        var randomChatId =
                String.valueOf(new Random().nextLong(111111111, 999999999));
        var message = Message.builder()
                .answer(randomString)
                .question(randomString)
                .user(User.builder()
                        .chatId(randomChatId)
                        .email("test@test" + randomChatId + ".com")
                        .username(randomString)
                        .organization(Organization.builder()
                                .name(randomString)
                                .address(randomString)
                                .build())
                        .build())
                .build();

        return messageService.createMessage(message);
    }


    @Test
    void testGetAll() throws Exception {
        var messages = messageService.getAllMessages();
        System.out.println(messages);
        mockHttpGet(API_PATH).andExpect(
                content().string(objectMapper.writeValueAsString(messages)));
    }

    @Test
    void testGetById() throws Exception {
        var message = messageService.getMessageById(1L);
        mockHttpGet(API_PATH + "/{id}", message.getId()).andExpect(
                content().string(
                        objectMapper.writeValueAsString(message)));
    }

    @Test
    void testCreateCascadeWithUser() throws Exception {
        var message = createMessage();
        mockHttpPost(API_PATH, message);
    }


    @Test
    void whenPassedExistingIdInPOST_ThenMessageShouldBeCreatedInsteadUpdate()
            throws Exception {
        var existingMessage = messageService.getMessageById(1L);
        var anotherMessage = Message.builder()
                .id(existingMessage.getId())
                .question("Another question")
                .user(existingMessage.getUser())
                .build();

        mockHttpPost(API_PATH, anotherMessage)
                .andExpect(
                        jsonPath("$.id").value(not(existingMessage.getId())));
    }

    @Test
    void whenPassedExistingUser_ThenOnlyMessageShouldCreatedNotCascade()
            throws Exception {
        var userFromDb = userService.findUserById(1L);
        var message = new Message(null, "Question", "Answer", userFromDb);

        mockHttpPost(API_PATH, message);
    }

    @Test
    void testUpdate() throws Exception {
        var message = messageService.getMessageById(1L);
        message.setQuestion("NEW_QUESTION");
        message.setAnswer("NEW_ANSWER");

        mockHttpPut(API_PATH + "/{id}", message.getId(), message)
                .andExpect(jsonPath("$.question").value(message.getQuestion()))
                .andExpect(jsonPath("$.answer").value(message.getAnswer()));
    }

    @Test
    void testDeleteById() throws Exception {
        var message = createMessage();

        mockHttpDelete(API_PATH + "/{id}", message.getId());
        assertThrows(EntityNotFoundException.class,
                () -> messageService.getMessageById(message.getId()));
    }

    @Test
    void testPartialUpdate() throws Exception {
        var message = messageService.getMessageById(1L);
        message.setQuestion("updatedQuestion");
        message.setAnswer("updatedAnswer");

        mvc.perform(patch(API_PATH + "/{id}", message.getId())
                        .contentType(CONTENT_TYPE)
                        .content("{\"question\":\"" + message.getQuestion() + "\"," +
                                "\"answer\":\"" + message.getAnswer() + "\"}")
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value(message.getQuestion()))
                .andExpect(jsonPath("$.answer").value(message.getAnswer()));
    }

    @Test
    void testFindMessagesByUserId() throws Exception {
        var message = messageService.getMessageById(1L);

        mockHttpGet(API_PATH + "/user/{chatId}", message.getUser().getChatId())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        Collections.singletonList(message))));
    }

}
