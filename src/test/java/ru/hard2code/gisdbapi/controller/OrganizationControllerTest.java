package ru.hard2code.gisdbapi.controller;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.HashMap;

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class OrganizationControllerTest extends
        AbstractControllerTestConfig {

    private static final String API_PATH =
            "/api" + Constants.Route.ORGANIZATIONS;


    @Autowired
    private OrganizationService organizationService;

    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    @Test
    void testFindAll() throws Exception {
        var organizations = organizationService.findAllOrganizations();

        mockHttpGet(API_PATH)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(organizations)));
    }

    @Test
    void testFindById() throws Exception {
        var organization = organizationService.findOrganizationById(1);

        mockHttpGet(API_PATH + "/{id}", 1)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(organization)));
    }

    @Test
    void testCreate() throws Exception {
        var organization = new Organization("Some name", "Some address");

        mockHttpPost(API_PATH, organization).andExpect(status().isOk());
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {
        var existingOrganization = organizationService.findOrganizationById(1);
        var anotherOrganization = Organization.builder()
                .id(existingOrganization.getId())
                .name("Another organization")
                .build();

        mockHttpPost(API_PATH, anotherOrganization)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(
                        not(existingOrganization.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var organization = organizationService.findOrganizationById(1);
        organization.setAddress("new address");
        organization.setName("new name");
        organization.setGovernment(false);

        var request = new HashMap<>() {{
            put("address", organization.getAddress());
            put("name", organization.getName());
            put("government", organization.isGovernment());
        }};

        mockHttpPut(API_PATH + "/{id}", organization.getId(),
                request)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(organization)));
    }

    @Test
    void testDeleteById() throws Exception {
        var organization = new Organization(RandomStringUtils.random(32),
                RandomStringUtils.random(32));

        organizationService.createOrganization(organization);

        mockHttpDelete(API_PATH + "/{id}", organization.getId()).andExpect(
                status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> organizationService.findOrganizationById(
                        organization.getId()));
    }

}
