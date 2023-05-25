package ru.hard2code.gisdbapi.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.service.category.CategoryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hard2code.gisdbapi.service.category.CategoryService.CACHE_LIST_KEY;
import static ru.hard2code.gisdbapi.service.category.CategoryService.CACHE_NAME;


class CategoryCacheTest extends AbstractCacheTestConfig {

    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    @AfterEach
    void cleanup() {
        clearAllCache();
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindAllCategoriesThenCacheShouldCreated() {
        var categories = categoryService.findAllCategories();

        assertEquals(categories.size(),
                ((List<Category>) cacheManager.getCache(CACHE_NAME)
                        .get(CACHE_LIST_KEY).get()).size());
    }

    @Test
    void whenDeleteCategoryByIdThenCacheWillEvict() {
        categoryService.findAllCategories();
        assertNotNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));

        categoryService.deleteCategoryById(1);
        assertNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));
    }

    @Test
    void whenCreateCategoryThenCacheWillEvict() {
        categoryService.findAllCategories();
        assertNotNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));

        categoryService.createCategory(
                new Category(RandomStringUtils.randomAlphabetic(12)));

        assertNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));
    }

}
