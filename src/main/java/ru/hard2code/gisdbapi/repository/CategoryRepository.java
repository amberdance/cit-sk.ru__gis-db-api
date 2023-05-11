package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
