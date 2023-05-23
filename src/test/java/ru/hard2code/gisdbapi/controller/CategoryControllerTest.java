package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.system.Constants;
import ru.hard2code.gisdbapi.util.RandomString;

import java.util.Collections;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class CategoryControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.CATEGORIES;


    @Autowired
    private CategoryService categoryService;


    private Category getCategoryWithRandomName() {
        return new Category(RandomString.generate());
    }

    @BeforeAll
    static void startContainer() {
        init();
    }

    @AfterAll
    static void stopContainer() {
        shutdown();
    }


    @Test
    void testCreate() throws Exception {
        var category = getCategoryWithRandomName();

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(category))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void whenPassedExistingIdInPOSTThenCategoryShouldBeCreatedInsteadUpdate()
            throws Exception {
        var category = getCategoryWithRandomName();

        categoryService.createCategory(new Category(RandomString.generate()));

        var anotherCategory =
                new Category(category.getId(), "Another category",
                        Collections.emptySet());

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(anotherCategory))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(not(category.getId())))
                .andExpect(jsonPath("$.name").value("Another category"));
    }


    @Test
    void testFindAll() throws Exception {
        var categories = categoryService.findAll();

        mvc.perform(get(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(categories)));
    }

    @Test
    void testFindById() throws Exception {
        var category = categoryService.findById(4);

        mvc.perform(get(API_PATH + "/{id}", category.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(category)));
    }

    @Test
    void testDeleteById() throws Exception {
        var id = 1;
        mvc.perform(delete(API_PATH + "/{id}", id)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(id));
    }

    @Test
    void testUpdate() throws Exception {
        var category = categoryService.findById(4);
        category.setName("NEW_NAME");

        mvc.perform(put(API_PATH + "/{id}", category.getId())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(category))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NEW_NAME"));
    }

}
