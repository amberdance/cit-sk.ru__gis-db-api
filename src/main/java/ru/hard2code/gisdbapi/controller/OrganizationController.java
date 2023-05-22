package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.service.organization.OrganizationService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.Route.ORGANIZATIONS)
@Tag(name = "OrganizationController", description = "Organizations " +
        "management" +
        " API")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<Organization> getAllOrganizations(@RequestParam(value =
            "isGovernment", required = false) Boolean isGovernment) {
        return isGovernment == null ? organizationService.findAll() :
                organizationService.findByType(isGovernment);
    }

    @GetMapping("{id}")
    public Organization getOrganizationById(@PathVariable("id") long id) {
        return organizationService.findById(id);
    }

    @PostMapping
    @Hidden
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationService.createOrganization(organization);
    }

    @PutMapping("{id}")
    @Hidden
    public Organization updateOrganization(@PathVariable("id") long id,
                                           @RequestBody Organization organization) {
        return organizationService.update(id, organization);
    }

    @DeleteMapping("{id}")
    @Hidden
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrganization(@PathVariable("id") long id) {
        organizationService.delete(id);
    }
}
