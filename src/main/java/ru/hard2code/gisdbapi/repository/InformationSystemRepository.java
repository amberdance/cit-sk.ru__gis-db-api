package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.model.InformationSystem;

import java.util.Optional;

public interface InformationSystemRepository extends JpaRepository<InformationSystem, Long> {
    Optional<InformationSystem> findByName(String name);

}