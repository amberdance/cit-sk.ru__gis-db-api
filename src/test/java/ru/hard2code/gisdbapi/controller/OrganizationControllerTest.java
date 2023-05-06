package ru.hard2code.gisdbapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTest extends ControllerTestConfig {

    private final Organization ORGANIZATION = new Organization(0, "org1", "org1", "org1", "org1");

    @Autowired
    private OrganizationService organizationService;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from organizations");
    }

    @Test
    void shouldFindAllOrganizations() throws Exception {
        var gisList = List.of(
                new Organization(0, "test1", "test1", "test1", "test1"),
                new Organization(0, "test2", "test2", "test2", "test2"),
                new Organization(0, "test3", "test3", "test3", "test3")
        );

        organizationService.createOrganization(gisList);

        mvc.perform(get("/organizations").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(gisList)))
                .andReturn();
    }

    @Test
    void shouldFinOrganizationdById() throws Exception {
        organizationService.createOrganization(ORGANIZATION);

        mvc.perform(get("/organizations/{id}", ORGANIZATION.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(ORGANIZATION)))
                .andReturn();
    }

    @Test
    void shouldCreateOrganization() throws Exception {
        mvc.perform(post("/organizations")
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldUpdateOrganization() throws Exception {
        organizationService.createOrganization(ORGANIZATION);
        ORGANIZATION.setShortName("1");
        ORGANIZATION.setRequisites("2");
        ORGANIZATION.setAddress("3");
        ORGANIZATION.setFullName("4");

        mvc.perform(put("/organizations/{id}", ORGANIZATION.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(ORGANIZATION)))
                .andReturn();
    }

    @Test
    void shouldDeleteOrganizationById() throws Exception {
        organizationService.createOrganization(ORGANIZATION);

        mvc.perform(delete("/organizations/{id}", ORGANIZATION.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        assertThrows(EntityNotFoundException.class, () -> organizationService.findById(ORGANIZATION.getId()));
    }

}