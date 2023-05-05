package ru.hard2code.GisDatabaseApi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.hard2code.GisDatabaseApi.model.Organization;
import ru.hard2code.GisDatabaseApi.service.organization.OrganizationService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrganizationControllerTest extends ControllerTestConfig {

    @Autowired
    private OrganizationService organizationService;

    @Test
    void shouldFindAll() throws Exception {
        var gisList = List.of(
                new Organization(0, "test1", "test1", "test1", "test1"),
                new Organization(0, "test2", "test2", "test2", "test2"),
                new Organization(0, "test3", "test3", "test3", "test3")
        );

        organizationService.create(gisList);

        mvc.perform(get("/organizations").accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(gisList)))
                .andReturn();
    }

    @Test
    void shouldFindById() throws Exception {
        var org = new Organization(0, "org1", "org1", "org1", "org1");
        organizationService.create(org);

        mvc.perform(get("/organizations/{id}", org.getId()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(org)))
                .andReturn();
    }

}