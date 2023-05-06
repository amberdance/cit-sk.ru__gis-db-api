package ru.hard2code.GisDatabaseApi.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.GisDatabaseApi.model.InformationSystem;
import ru.hard2code.GisDatabaseApi.service.informationSystem.InformationSystemService;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InformationSystemControllerTest extends ControllerTestConfig {

    @Autowired
    private InformationSystemService informationSystemService;


    private final InformationSystem TEST_INFORMATION_SYSTEM = new InformationSystem(0, "someName");

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from information_systems");
    }

    @Test
    void shouldCreateInformationSystem() throws Exception {
        mvc.perform(post("/information-systems")
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andReturn();
    }
}