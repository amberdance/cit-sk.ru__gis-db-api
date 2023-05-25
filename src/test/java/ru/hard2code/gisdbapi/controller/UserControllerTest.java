package ru.hard2code.gisdbapi.controller;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
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

import java.util.HashMap;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class UserControllerTest extends
        AbstractControllerTestConfig {

    private static final String API_PATH = "/api" + Constants.Route.USERS;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;


    private User getRandomUser() {
        var randomString = RandomStringUtils.randomAlphabetic(16);
        var randomChatId =
                String.valueOf(RandomUtils.nextLong(100000000, 1000000000));

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
        CONTAINER.start();
    }

    @Test
    void testFindById() throws Exception {
        var user = userService.findUserById(1L);
        mockHttpGet(API_PATH + "/{id}", user.getId())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(user)));
    }

    @Test
    void testFindAll() throws Exception {
        var users = userService.findAllUsers();
        mockHttpGet(API_PATH)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(users)));
    }

    @Test
    void testDeleteById() throws Exception {
        var user = userService.createUser(getRandomUser());

        mockHttpDelete(API_PATH + "/{id}", user.getId()).andExpect(
                status().isNoContent());
        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserById(user.getId())
        );
    }

    @Test
    void testCreateCascade() throws Exception {
        mockHttpPost(API_PATH, getRandomUser()).andExpect(status().isOk());
    }

    @Test
    void testCreateWithExistingOrganization() throws Exception {
        var user = getRandomUser();
        user.setOrganization(organizationService.findOrganizationById(2));

        mockHttpPost(API_PATH, user).andExpect(status().isOk());
    }

    @Test
    void whenPassedExistingIdInPOST_ThenMessageShouldBeCreatedInsteadUpdate()
            throws Exception {
        var existingUser = userService.findUserById(1L);
        var anotherUserWithSameId = getRandomUser().toBuilder()
                .id(existingUser.getId())
                .build();

        mockHttpPost(API_PATH, anotherUserWithSameId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(not(existingUser.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var user = userService.createUser(getRandomUser());
        user = user.toBuilder()
                .chatId("98989796959")
                .email("some@email.mail")
                .username("ambr")
                .role(Role.USER)
                .build();

        mockHttpPut(API_PATH + "/{id}", user.getId(), user)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(user)));
    }

    @Test
    void testValidation() throws Exception {
        var wrongUser = User.builder()
                .chatId("not_valid_param")
                .email("not_valid_param")
                .organization(new Organization("Organization", "Address"))
                .build();

        mockHttpPost(API_PATH, wrongUser).andExpect(status().isBadRequest());
    }

    @Test
    void testPartialUpdate() throws Exception {
        var user = userService.findUserById(1);

        user.setChatId("1231457892");
        user.setEmail("email@nothere.ru");
        user.setOrganization(organizationService.findOrganizationById(2));
        var request = new HashMap<>() {{
            put("chatId", user.getChatId());
            put("email", user.getEmail());
            put("organization", user.getOrganization());
        }};

        mockHttpPatch(API_PATH + "/{id}", user.getId(), request)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(user)));

    }

}
