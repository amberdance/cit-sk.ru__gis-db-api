package ru.hard2code.gisdbapi.service.organization;

import ru.hard2code.gisdbapi.model.Organization;

import java.util.List;

public interface OrganizationService {
    List<Organization> findAll();

    Organization createOrganization(Organization organization);

    Organization findById(long id);

    Organization update(long id, Organization organization);

    void delete(long id);

    List<Organization> findByType(boolean isGovernment);

    void deleteAll();
}
