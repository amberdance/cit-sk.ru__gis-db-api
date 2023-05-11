package ru.hard2code.gisdbapi.service.organization;


import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Organization;
import ru.hard2code.gisdbapi.repository.OrganizationRepository;

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
    public Organization createOrganization(Organization org) {
        return organizationRepository.save(org);
    }

    @Override
    public Organization findById(long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Organization.class, id));
    }

    @Override
    public Organization update(long id, Organization organization) {
        var org = organizationRepository.findById(id)
                .orElseGet(() -> createOrganization(organization));
        org.setName(organization.getName());
        org.setAddress(organization.getAddress());
        org.setGovernment(org.isGovernment());

        return organizationRepository.save(org);
    }

    @Override
    public void delete(long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public List<Organization> findByType(boolean isGovernment) {
        return organizationRepository.findByIsGovernment(isGovernment);
    }

    @Override
    public void deleteAll() {
        organizationRepository.deleteAllInBatch();
    }
}
