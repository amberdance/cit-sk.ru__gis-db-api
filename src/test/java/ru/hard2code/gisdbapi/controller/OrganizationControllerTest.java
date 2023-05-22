package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class OrganizationControllerTest extends AbstractControllerTest {

    private static final String API_PATH =
            "/api" + Constants.Route.ORGANIZATIONS;

    private static final Organization TEST_ORGANIZATION = new Organization("name", "address");

    @Autowired
    private OrganizationService organizationService;

    @AfterEach
    void cleanup() {
        organizationService.deleteAll();
    }

    @Test
    void testFindAll() throws Exception {
        var organizations = List.of(
                new Organization("name1", "name1"),
                new Organization("name2", "name2"),
                new Organization("name3", "name3")
        );

        organizations.forEach(organizationService::createOrganization);

        mvc.perform(get(API_PATH).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(organizations)));
    }

    @Test
    void testFindById() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        mvc.perform(get(API_PATH + "/{id}", TEST_ORGANIZATION.getId())
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(TEST_ORGANIZATION)));
    }

    @Test
    void testCreate() throws Exception {
        mvc.perform(post(API_PATH).contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(TEST_ORGANIZATION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        var anotherOrganization = Organization.builder()
                .id(TEST_ORGANIZATION.getId())
                .name("Another organization")
                .build();

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(anotherOrganization))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(not(TEST_ORGANIZATION.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        TEST_ORGANIZATION.setName("NEW_NAME");
        TEST_ORGANIZATION.setAddress("NEW_ADDRESS");

        mvc.perform(put(API_PATH + "/{id}", TEST_ORGANIZATION.getId())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(TEST_ORGANIZATION))
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value(TEST_ORGANIZATION.getName()))
           .andExpect(jsonPath("$.address").value(TEST_ORGANIZATION.getAddress()));
    }

    @Test
    void testDeleteById() throws Exception {
        organizationService.createOrganization(TEST_ORGANIZATION);

        mvc.perform(delete(API_PATH + "/{id}", TEST_ORGANIZATION.getId())
                   .accept(CONTENT_TYPE))
           .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> organizationService.findById(TEST_ORGANIZATION.getId()));
    }

}
