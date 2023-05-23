package ru.hard2code.gisdbapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.service.category.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hard2code.gisdbapi.service.category.CategoryService.CACHE_LIST_KEY;
import static ru.hard2code.gisdbapi.service.category.CategoryService.CACHE_NAME;

class CategoryCacheTest extends AbstractCacheTest<Category> {

    @Autowired
    private CategoryService categoryService;


    private void createInstances() {
        categoryService.deleteAllCategories();

        for (int i = 0; i < INSTANCES_COUNT; i++) {
            INSTANCES.add(categoryService.createCategory(
                    new Category(String.valueOf(i))));
        }
    }

    @BeforeEach
    void setUp() {
        createInstances();
        categoryService.findAll();
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindAllCategoriesThenCacheShouldCreated() {
        assertEquals(INSTANCES.size(),
                ((List<Category>) cacheManager.getCache(CACHE_NAME)
                        .get(CACHE_LIST_KEY).get()).size());
    }

    @Test
    void whenDeleteCategoryByIdThenCacheWillEvict() {
        var id = INSTANCES.get(0).getId();
        categoryService.deleteCategoryById(id);

        assertNull(cacheManager.getCache(CACHE_NAME).get(id));
    }

    @Test
    void whenCreateCategoryThenCacheWillEvict() {
        var cacheBefore = cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY);

        categoryService.createCategory(new Category("test"));

        assertNotEquals(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY),
                cacheBefore);
    }

    @Test
    void whenFindCategoryByIdTheCategoryWillReturnedFromCache() {
        var id = INSTANCES.get(0).getId();
        categoryService.findById(id);

        assertNotNull(cacheManager.getCache(CACHE_NAME).get(id));
    }


}
