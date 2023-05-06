package ru.hard2code.GisDatabaseApi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.GisDatabaseApi.model.InformationSystem;

public interface InformationSystemRepository extends JpaRepository<InformationSystem, Long> {
}