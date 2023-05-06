package ru.hard2code.gisdbapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.service.informationSystem.InformationSystemService;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InformationSystemControllerTest extends ControllerTestConfig {

    @Autowired
    private InformationSystemService informationSystemService;


    private final InformationSystem TEST_INFORMATION_SYSTEM = new InformationSystem("someName");

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

    @Test
    void shouldFindAllInformationSystems() throws Exception {
        var systems = List.of(new InformationSystem("1"), new InformationSystem("2"), new InformationSystem("3"));

        informationSystemService.createInformationSystem(systems);

        mvc.perform(get("/information-systems")
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk()).andExpect(content().string(OBJECT_MAPPER.writeValueAsString(systems)))
                .andReturn();
    }

    @Test
    void shouldFindInformationSystemById() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        mvc.perform(get("/information-systems/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM)))
                .andReturn();
    }

    @Test
    void shouldDeleteInformationSystemById() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        mvc.perform(delete("/information-systems/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> informationSystemService.findById(TEST_INFORMATION_SYSTEM.getId()));
    }

    @Test
    void shouldUpdateInformationSystem() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        TEST_INFORMATION_SYSTEM.setName("NEW_NAME");

        mvc.perform(put("/information-systems/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM)).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value("NEW_NAME"))
                .andReturn();
    }

}