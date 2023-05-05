package ru.hard2code.GisDatabaseApi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.GisDatabaseApi.model.Organization;
import ru.hard2code.GisDatabaseApi.service.organization.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    List<Organization> getAllOrganizations() {
        return organizationService.findAll();
    }

    @GetMapping("{id}")
    Organization getOrganizationById(@PathVariable("id") long id) {
        return organizationService.findById(id);
    }
}
