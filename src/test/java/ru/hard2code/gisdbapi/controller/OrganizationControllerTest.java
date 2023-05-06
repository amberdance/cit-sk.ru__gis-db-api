package ru.hard2code.gisdbapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTest extends ControllerTestConfig {

    private final Organization TEST_ORGANIZATION = new Organization("name", "address");

    @Autowired
    private OrganizationServiceImpl governmentOrganizationServiceServiceImpl;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from organizations");
    }

    @Test
    void shouldFindAllOrganizations() throws Exception {
        var gisList = List.of(
                new Organization("name1", "name1"),
                new Organization("name2", "name2"),
                new Organization("name3", "name3")
        );

        governmentOrganizationServiceServiceImpl.createOrganization(gisList);

        mvc.perform(get("/organizations").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(gisList)))
                .andReturn();
    }

    @Test
    void shouldFinOrganizationdById() throws Exception {
        governmentOrganizationServiceServiceImpl.createOrganization(TEST_ORGANIZATION);

        mvc.perform(get("/organizations/{id}", TEST_ORGANIZATION.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION)))
                .andReturn();
    }

    @Test
    void shouldCreateOrganization() throws Exception {
        mvc.perform(post("/organizations")
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldUpdateOrganization() throws Exception {
        governmentOrganizationServiceServiceImpl.createOrganization(TEST_ORGANIZATION);

        TEST_ORGANIZATION.setName("NEW_NAME");
        TEST_ORGANIZATION.setAddress("NEW_ADDRESS");

        mvc.perform(put("/organizations/{id}", TEST_ORGANIZATION.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION)))
                .andReturn();
    }

    @Test
    void shouldDeleteOrganizationById() throws Exception {
        governmentOrganizationServiceServiceImpl.createOrganization(TEST_ORGANIZATION);

        mvc.perform(delete("/organizations/{id}", TEST_ORGANIZATION.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> governmentOrganizationServiceServiceImpl.findById(TEST_ORGANIZATION.getId()));
    }

}