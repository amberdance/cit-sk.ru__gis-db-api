package ru.hard2code.GisDatabaseApi.service.organization;

import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.Organization;
import ru.hard2code.GisDatabaseApi.repository.OrganizationRepository;
import ru.hard2code.GisDatabaseApi.service.organization.OrganizationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository repository;

    public OrganizationServiceImpl(OrganizationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Organization> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Organization> saveAll(List<Organization> gisList) {
        List<Organization> result = new ArrayList<>();
        gisList.forEach(repository::save);

        return result;
    }
}
