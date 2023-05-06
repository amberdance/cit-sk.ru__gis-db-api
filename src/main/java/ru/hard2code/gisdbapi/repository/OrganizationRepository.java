package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.model.Organization;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByIsGovernment(boolean isGovernment);
}
