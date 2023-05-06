package ru.hard2code.gisdbapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationService.findAll();
    }

    @GetMapping("{id}")
    public Organization getOrganizationById(@PathVariable("id") long id) {
        return organizationService.findById(id);
    }

    @PostMapping
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationService.createOrganization(organization);
    }

    @PutMapping("{id}")
    public Organization updateOrganization(@PathVariable("id") long id, @RequestBody Organization organization) {
        return organizationService.update(id, organization);
    }

    @DeleteMapping("{id}")
    public void updateOrganization(@PathVariable("id") long id) {
        organizationService.delete(id);
    }
}
