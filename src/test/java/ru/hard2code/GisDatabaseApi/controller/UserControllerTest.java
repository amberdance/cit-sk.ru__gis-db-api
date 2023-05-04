package ru.hard2code.GisDatabaseApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.hard2code.GisDatabaseApi.model.User;
import ru.hard2code.GisDatabaseApi.model.UserType;
import ru.hard2code.GisDatabaseApi.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {
        var expectedUser = new User(null, "chatId", new UserType(null, UserType.Type.CITIZEN));
        var objectMapper = new ObjectMapper();

        userService.save(expectedUser);

        System.out.println("--------------------------");
        System.out.println(objectMapper.writeValueAsString(expectedUser));
        System.out.println("--------------------------");
        System.out.println(expectedUser);

        mvc.perform(get("/api/users/{id}", 1L).accept("application/json; charset=utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));
    }
}