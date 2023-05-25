package ru.hard2code.gisdbapi.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

import java.util.HashMap;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class MessageControllerTest extends
        AbstractControllerTestConfig {

    private static final String API_PATH = "/api" + Constants.Route.MESSAGES;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    private Message getRandomMessage() {
        var randomString = RandomStringUtils.randomAlphabetic(16);
        var randomChatId =
                String.valueOf(RandomUtils.nextLong(100000000, 1000000000));

        return Message.builder()
                .answer(randomString)
                .question(randomString)
                .user(User.builder()
                        .chatId(randomChatId)
                        .email("test@test" + randomChatId + ".ru")
                        .username(randomString)
                        .organization(Organization.builder()
                                .name(randomString)
                                .address(randomString)
                                .build())
                        .build())
                .build();
    }


    @Test
    void testGetAll() throws Exception {
        var messages = messageService.findAllMessages();
        mockHttpGet(API_PATH)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(messages)));
    }

    @Test
    void testGetById() throws Exception {
        var message = messageService.findMessageById(1);
        mockHttpGet(API_PATH + "/{id}", message.getId())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(message)));
    }

    @Test
    void testCreateCascade() throws Exception {
        mockHttpPost(API_PATH, getRandomMessage()).andExpect(
                status().isOk());
    }


    @Test
    void whenPassedExistingIdInPOST_ThenMessageShouldBeCreatedInsteadUpdate()
            throws Exception {
        var existingMessage = messageService.findMessageById(1L);
        var anotherMessage = Message.builder()
                .id(existingMessage.getId())
                .question("Another question")
                .user(existingMessage.getUser())
                .build();

        mockHttpPost(API_PATH, anotherMessage)
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(not(existingMessage.getId())));
    }

    @Test
    void whenPassedExistingUser_ThenOnlyMessageShouldCreated()
            throws Exception {
        var userFromDb = userService.findUserById(1L);
        var message = new Message(null, "Question", "Answer", userFromDb);

        mockHttpPost(API_PATH, message).andExpect(status().isOk());
    }

    @Test
    void testPatchUpdate() throws Exception {
        var message = messageService.findMessageById(1L);

        message.setQuestion("NEW_QUESTION");
        message.setAnswer("NEW_ANSWER");
        var request = new HashMap<>() {{
            put("question", message.getQuestion());
            put("answer", message.getAnswer());
        }};

        mockHttpPatch(API_PATH + "/{id}", message.getId(), request)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(message)));
    }

    @Test
    void testDeleteById() throws Exception {
        var message = getRandomMessage();
        messageService.createMessage(message);

        mockHttpDelete(API_PATH + "/{id}", message.getId()).andExpect(
                status().isNoContent());
        assertThrows(EntityNotFoundException.class,
                () -> messageService.findMessageById(message.getId()));
    }


    @Test
    void testFindMessagesByChatId() throws Exception {
        var chatId = userService.findUserById(1).getChatId();
        var messages = messageService.findMessageByChatId(chatId);

        mockHttpGet(API_PATH + "/user/{chatId}", chatId)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(messages)));
    }

}
