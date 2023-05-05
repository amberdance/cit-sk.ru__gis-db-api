package ru.hard2code.GisDatabaseApi.service;

import ru.hard2code.GisDatabaseApi.model.Organization;

import java.util.List;

public interface OrganizationService {
    List<Organization> findAll();

    List<Organization> saveAll(List<Organization> gisList);
}
