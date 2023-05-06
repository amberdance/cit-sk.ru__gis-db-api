package ru.hard2code.gisdbapi.service.organization;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.repository.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository governmentOrganizationRepository) {
        this.organizationRepository = governmentOrganizationRepository;
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization createOrganization(Organization org) {
        System.out.println(org);
        return organizationRepository.save(org);
    }

    @Override
    public List<Organization> createOrganization(List<Organization> organizations) {
        List<Organization> result = new ArrayList<>();
        organizations.forEach(org -> result.add(createOrganization(org)));

        return result;
    }

    @Override
    public Organization findById(long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find organization with id " + id));
    }

    @Override
    public Organization update(long id, Organization organization) {
        var org = organizationRepository.findById(id).orElseGet(() -> createOrganization(organization));
        org.setName(organization.getName());
        org.setAddress(organization.getAddress());
        org.setGovernment(org.isGovernment());

        return org;
    }

    @Override
    public void delete(long id) {
        organizationRepository.deleteById(id);
    }
}
