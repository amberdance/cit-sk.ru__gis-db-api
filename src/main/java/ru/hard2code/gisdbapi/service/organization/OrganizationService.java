package ru.hard2code.gisdbapi.service.organization;

import ru.hard2code.gisdbapi.domain.entity.Organization;

import java.util.List;

public interface OrganizationService {
    List<Organization> findAllOrganizations();

    List<Organization> findOrganizationsByType(boolean isGovernment);

    Organization createOrganization(Organization organization);

    Organization findOrganizationById(long id);

    Organization updateOrganization(long id, Organization organization);

    void deleteOrganizationById(long id);


}
