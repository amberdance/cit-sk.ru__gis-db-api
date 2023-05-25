package ru.hard2code.gisdbapi.service.category;

import ru.hard2code.gisdbapi.domain.entity.Category;

import java.util.List;

public interface CategoryService {

    String CACHE_NAME = "categories";
    String CACHE_LIST_KEY = "categoriesList";

    List<Category> findAllCategories();

    Category findCategoryById(long id);

    Category createCategory(Category gis);

    Category updateCategory(long id, Category category);

    void deleteCategoryById(long id);

}
