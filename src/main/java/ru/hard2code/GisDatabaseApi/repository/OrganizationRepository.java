package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.GisDatabaseApi.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
