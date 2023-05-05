package ru.hard2code.GisDatabaseApi.service.organization;

import ru.hard2code.GisDatabaseApi.model.Organization;

import java.util.List;

public interface OrganizationService {
    List<Organization> findAll();

    Organization create(Organization org);

    List<Organization> create(List<Organization> gisList);

    Organization findById(long id);


}
