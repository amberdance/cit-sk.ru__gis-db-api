package ru.hard2code.gisdbapi.controller;


import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.Collections;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(authorities = {"write", "read"})
class CategoryControllerTest extends AbstractTestContainersControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.CATEGORIES;


    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void setup() {
        startContainer();
    }

    private Category createRandomCategory() {
        return categoryService.createCategory(
                new Category(RandomStringUtils.random(32)));
    }

    @Test
    void testCreate() throws Exception {
        var category = new Category(RandomStringUtils.random(32));
        mockHttpPost(API_PATH, category).andExpect(
                jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void whenPassedExistingIdInPOSTThenCategoryShouldBeCreatedInsteadUpdate()
            throws Exception {
        var category = createRandomCategory();
        var anotherCategory =
                new Category(category.getId(), "Another category",
                        Collections.emptySet());

        mockHttpPost(API_PATH, anotherCategory).andExpect(
                        jsonPath("$.id").value(not(category.getId())))
                .andExpect(jsonPath("$.name").value("Another category"));
    }


    @Test
    void testFindAll() throws Exception {
        var categories = categoryService.findAll();
        mockHttpGet(API_PATH).andExpect(
                content().string(objectMapper.writeValueAsString(categories)));
    }

    @Test
    void testFindById() throws Exception {
        var category = categoryService.findById(1);

        mockHttpGet(API_PATH + "/{id}", category.getId()).andExpect(
                content().string(objectMapper.writeValueAsString(category)));
    }

    @Test
    void testDeleteById() throws Exception {
        var category = createRandomCategory();

        mockHttpDelete(API_PATH + "/{id}", category.getId());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(category.getId()));
    }

    @Test
    void testUpdate() throws Exception {
        var category = categoryService.findById(1);
        category.setName("NEW_NAME");

        mockHttpPut(API_PATH + "/{id}", category.getId(), category).andExpect(
                jsonPath("$.name").value("NEW_NAME"));
    }

}
