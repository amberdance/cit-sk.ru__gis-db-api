package ru.hard2code.gisdbapi.controller;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.domain.entity.Role;
import ru.hard2code.gisdbapi.domain.entity.User;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;
import ru.hard2code.gisdbapi.service.user.UserService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.Random;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class UserControllerTest extends AbstractTestContainersControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.USERS;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    private User createRandomUser() {
        var randomString = RandomStringUtils.random(32);
        var randomChatId =
                String.valueOf(new Random().nextLong(111111111, 999999999));

        return User.builder()
                .chatId(randomChatId)
                .username(randomString)
                .email("test@test" + randomString + ".ru")
                .role(Role.ADMIN)
                .organization(new Organization(randomString, randomString))
                .build();
    }

    @BeforeAll
    static void setup() {
        startContainer();
    }

    @Test
    void testFindById() throws Exception {
        var user = userService.findUserById(1L);
        mockHttpGet(API_PATH + "/{id}", user.getId()).andExpect(
                content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    void testFindAll() throws Exception {
        var users = userService.findAllUsers();
        mockHttpGet(API_PATH).andExpect(
                content().string(objectMapper.writeValueAsString(users)));
    }

    @Test
    void testDeleteById() throws Exception {
        var user = userService.createUser(createRandomUser());

        mockHttpDelete(API_PATH + "/{id}", user.getId());
        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserById(user.getId())
        );
    }

    @Test
    void testCreate() throws Exception {
        mockHttpPost(API_PATH, createRandomUser());
    }

    @Test
    void whenPassedExistingIdInPOST_ThenMessageShouldBeCreatedInsteadUpdate()
            throws Exception {
        var existingUser = userService.findUserById(1L);
        var anotherUserWithSameId = createRandomUser().toBuilder()
                .id(existingUser.getId())
                .chatId("1234567899")
                .username("somename")
                .build();

        mockHttpPost(API_PATH, anotherUserWithSameId).andExpect(
                jsonPath("$.id").value(not(existingUser.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var user = userService.createUser(createRandomUser());

        mockHttpPut(API_PATH + "/{id}", user.getId(), user)
                .andExpect(jsonPath("$.chatId").value(user.getChatId()))
                .andExpect(jsonPath("$.role").value(user.getRole()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void testValidation() throws Exception {
        var wrongUser = User.builder()
                .chatId("test")
                .email("test")
                .build();

        mockHttpPost(API_PATH, wrongUser).andExpect(status().isBadRequest());
    }

    @Test
    void testPartialUpdate() throws Exception {
        var user = userService.createUser(createRandomUser());
        user.setChatId("1231457892");

        mvc.perform(patch(API_PATH + "/{id}", user.getId())
                        .contentType(CONTENT_TYPE)
                        .content("{\"chatId\":\"" + user.getChatId() + "\"}")
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(user.getChatId()));
    }

}
