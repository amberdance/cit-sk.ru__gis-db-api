package ru.hard2code.gisdbapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hard2code.gisdbapi.domain.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
