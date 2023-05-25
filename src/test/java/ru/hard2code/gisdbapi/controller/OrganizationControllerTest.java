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

import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(authorities = {"write", "read"})
class OrganizationControllerTest extends AbstractTestContainersControllerTest {

    private static final String API_PATH =
            "/api" + Constants.Route.ORGANIZATIONS;


    @Autowired
    private OrganizationService organizationService;

    @BeforeAll
    static void setup() {
        startContainer();
    }

    @Test
    void testFindAll() throws Exception {
        var organizations = organizationService.findAll();

        mockHttpGet(API_PATH).andExpect(content().string(
                objectMapper.writeValueAsString(organizations)));
    }

    @Test
    void testFindById() throws Exception {
        var organization = organizationService.findById(1L);

        mockHttpGet(API_PATH + "/{id}", 1L).andExpect(content().string(
                objectMapper.writeValueAsString(organization)));
    }

    @Test
    void testCreate() throws Exception {
        var name = RandomStringUtils.random(32);
        var organization = new Organization(name, name);

        mockHttpPost(API_PATH, organization);
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {
        var existingOrganization = organizationService.findById(1);
        var anotherOrganization = Organization.builder()
                .id(existingOrganization.getId())
                .name("Another organization")
                .build();

        mockHttpPost(API_PATH, anotherOrganization).andExpect(
                jsonPath("$.id").value(not(existingOrganization.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var organization = organizationService.findById(1);
        organization.setAddress("new address");
        organization.setName("new name");

        mockHttpPut(API_PATH + "/{id}", organization.getId(),
                organization).andExpect(
                        jsonPath("$.name").value("new name"))
                .andExpect(
                        jsonPath("$.address").value("new address"));
    }

    @Test
    void testDeleteById() throws Exception {
        var organization = new Organization(RandomStringUtils.random(32),
                RandomStringUtils.random(32));

        organizationService.createOrganization(organization);

        mockHttpDelete(API_PATH + "/{id}", organization.getId());
        assertThrows(EntityNotFoundException.class,
                () -> organizationService.findById(organization.getId()));
    }

}
