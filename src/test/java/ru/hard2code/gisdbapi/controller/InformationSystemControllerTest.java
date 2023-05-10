package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.service.informationSystem.InformationSystemService;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
class InformationSystemControllerTest extends AbstractControllerTest {


    private static final String API_PATH = "/api/information-systems";

    private final InformationSystem TEST_INFORMATION_SYSTEM =
            new InformationSystem("someName");

    @Autowired
    private InformationSystemService informationSystemService;


    @AfterEach
    void cleanup() {
        informationSystemService.deleteAllInformationSystems();
    }

    @Test
    void testCreate() throws Exception {
        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void testFindAll() throws Exception {
        var systems = List.of(new InformationSystem("1"),
                new InformationSystem("2"),
                new InformationSystem("3"));

        informationSystemService.createInformationSystem(systems.get(0));
        informationSystemService.createInformationSystem(systems.get(1));
        informationSystemService.createInformationSystem(systems.get(2));

        mvc.perform(get(API_PATH).contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(systems)));
    }

    @Test
    void testFindById() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        mvc.perform(get(API_PATH + "/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM)));
    }

    @Test
    void testDeleteById() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        mvc.perform(delete(API_PATH + "/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> informationSystemService.findById(TEST_INFORMATION_SYSTEM.getId()));
    }

    @Test
    void testUpdate() throws Exception {
        informationSystemService.createInformationSystem(TEST_INFORMATION_SYSTEM);

        TEST_INFORMATION_SYSTEM.setName("NEW_NAME");

        mvc.perform(put(API_PATH + "/{id}", TEST_INFORMATION_SYSTEM.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_INFORMATION_SYSTEM))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value("NEW_NAME"));
    }

}
