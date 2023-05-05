package ru.hard2code.GisDatabaseApi.service.organization;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.GisDatabaseApi.model.Organization;
import ru.hard2code.GisDatabaseApi.repository.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization create(Organization org) {
        System.out.println(org);
        return organizationRepository.save(org);
    }

    @Override
    public List<Organization> create(List<Organization> gisList) {
        List<Organization> result = new ArrayList<>();
        gisList.forEach(org -> result.add(create(org)));

        return result;
    }

    @Override
    public Organization findById(long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Organization with id " + id + " was not found"));
    }

    @Override
    public Organization update(long id, Organization organization) {
        var org = organizationRepository.findById(id).orElseGet(() -> create(organization));

        org.setAddress(organization.getAddress());
        org.setFullName(organization.getFullName());
        org.setRequisites(organization.getRequisites());
        org.setShortName(organization.getShortName());

        return org;
    }

    @Override
    public void delete(long id) {
        organizationRepository.deleteById(id);
    }
}
