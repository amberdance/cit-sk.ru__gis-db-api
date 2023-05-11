package ru.hard2code.gisdbapi.service.category;

import ru.hard2code.gisdbapi.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category gis);

    List<Category> findAll();

    Category findById(long id);

    void delete(long id);

    Category update(long id, Category category);


    void deleteAllCategories();
}
