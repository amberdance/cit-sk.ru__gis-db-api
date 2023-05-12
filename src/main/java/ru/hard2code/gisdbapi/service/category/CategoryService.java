package ru.hard2code.gisdbapi.service.category;

import ru.hard2code.gisdbapi.model.Category;

import java.util.List;

public interface CategoryService {

    String CACHE_NAME = "categories";
    String CACHE_LIST_KEY = "categoriesList";

    Category createCategory(Category gis);

    List<Category> findAll();

    Category findById(long id);

    void deleteCategoryById(long id);

    Category updateCategory(long id, Category category);


    void deleteAllCategories();
}
