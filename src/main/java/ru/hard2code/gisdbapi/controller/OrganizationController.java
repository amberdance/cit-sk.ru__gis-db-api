package ru.hard2code.gisdbapi.controller;

import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationServiceImpl organizationServiceImpl;

    OrganizationController(OrganizationServiceImpl organizationServiceImpl) {
        this.organizationServiceImpl = organizationServiceImpl;
    }

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationServiceImpl.findAll();
    }

    @GetMapping("{id}")
    public Organization getOrganizationById(@PathVariable("id") long id) {
        return organizationServiceImpl.findById(id);
    }

    @PostMapping
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationServiceImpl.createOrganization(organization);
    }

    @PutMapping("{id}")
    public Organization updateOrganization(@PathVariable("id") long id, @RequestBody Organization organization) {
        return organizationServiceImpl.update(id, organization);
    }

    @DeleteMapping("{id}")
    public void updateOrganization(@PathVariable("id") long id) {
        organizationServiceImpl.delete(id);
    }
}
