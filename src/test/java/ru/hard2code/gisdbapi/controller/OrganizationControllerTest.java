package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTest extends ControllerTestConfig {

    private final Organization TEST_ORGANIZATION = new Organization("name", "address");

    @Autowired
    private OrganizationService organizationService;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("delete from organizations");
    }

    @Test
    void shouldFindAllOrganizations() throws Exception {
        var orgs = List.of(
                new Organization("name1", "name1"),
                new Organization("name2", "name2"),
                new Organization("name3", "name3")
        );

        organizationService.createOrganization(orgs.get(0));
        organizationService.createOrganization(orgs.get(1));
        organizationService.createOrganization(orgs.get(2));

        mvc.perform(get("/organizations")
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(orgs)));
    }

    @Test
    void shouldFinOrganizationdById() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        mvc.perform(get("/organizations/{id}", TEST_ORGANIZATION.getId())
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION)));
    }

    @Test
    void shouldCreateOrganization() throws Exception {
        mvc.perform(post("/organizations")
                        .with(user(TEST_USER_ROLE))
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateOrganization() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        TEST_ORGANIZATION.setName("NEW_NAME");
        TEST_ORGANIZATION.setAddress("NEW_ADDRESS");

        mvc.perform(put("/organizations/{id}", TEST_ORGANIZATION.getId())
                        .with(user(TEST_USER_ROLE))
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_ORGANIZATION)));
    }

    @Test
    void shouldDeleteOrganizationById() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        mvc.perform(delete("/organizations/{id}", TEST_ORGANIZATION.getId())
                        .with(user(TEST_USER_ROLE))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class, () -> organizationService.findById(TEST_ORGANIZATION.getId()));
    }

}