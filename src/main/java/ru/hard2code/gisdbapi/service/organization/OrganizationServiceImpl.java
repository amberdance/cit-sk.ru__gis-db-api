package ru.hard2code.gisdbapi.service.organization;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Organization;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.OrganizationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;


    @Override
    public List<Organization> findAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization createOrganization(Organization org) {
        org.setId(null);
        return organizationRepository.save(org);
    }

    @Override
    public Organization findOrganizationById(long id) {
        return organizationRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Organization.class,
                                id));
    }

    @Override
    public Organization updateOrganization(long id, Organization org) {
        var optional = organizationRepository.findById(id);

        if (optional.isEmpty()) {
            return createOrganization(org);
        }

        var organization = optional.get()
                .toBuilder()
                .name(org.getName())
                .address(org.getAddress())
                .isGovernment(org.isGovernment())
                .build();

        return organizationRepository.save(organization);
    }

    @Override
    public void deleteOrganizationById(long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public List<Organization> findOrganizationsByType(boolean isGovernment) {
        return organizationRepository.findByIsGovernment(isGovernment);
    }

}
