package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/organizations")
@Tag(name = "OrganizationController", description = "Organizations management" +
        " API")
public class OrganizationController {

    private final OrganizationService organizationService;

    OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public List<Organization> getAllOrganizations(@RequestParam(value = "isGovernment", required = false) Boolean isGovernment) {
        return isGovernment == null ? organizationService.findAll() : organizationService.findByType(isGovernment);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrganization(@PathVariable("id") long id) {
        organizationService.delete(id);
    }
}
