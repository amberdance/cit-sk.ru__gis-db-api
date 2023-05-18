package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = {"write", "read"})
class QuestionControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api/" + Route.QUESTIONS;
    private final Category GOS_WEB_CATEGORY = new Category("GOSWEB");
    private final Category POS_WIDGET_CATEGORY = new Category("POS_WIDGET");
    private final Question TEST_QUESTION = new Question(null, "q1", "a1", GOS_WEB_CATEGORY);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    @BeforeEach
    void beforeEach() {
        categoryService.createCategory(GOS_WEB_CATEGORY);
        categoryService.createCategory(POS_WIDGET_CATEGORY);
    }

    @AfterEach
    void cleanUp() {
        categoryService.deleteAllCategories();
    }

    @Test
    void testFindAll() throws Exception {
        var q1 = new Question(null, "q1", "1", POS_WIDGET_CATEGORY);
        var q2 = new Question(null, "q2", "2", GOS_WEB_CATEGORY);

        questionService.createQuestion(q1);
        questionService.createQuestion(q2);

        mvc.perform(get(API_PATH)
                   .contentType(CONTENT_TYPE)
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(List.of(q1, q2))));
    }

    @Test
    void testFindById() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(get(API_PATH + "/{id}", TEST_QUESTION.getId())
                   .contentType(CONTENT_TYPE)
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION)));
    }

    @Test
    void testCreate() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(post(API_PATH)
                   .contentType(CONTENT_TYPE)
                   .content(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION))
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(notNullValue()))
           .andExpect(jsonPath("$.category.id").value(notNullValue()));
    }

    @Test
    void testUpdate() throws Exception {
        questionService.createQuestion(TEST_QUESTION);
        TEST_QUESTION.setLabel("NEW_LABEL");
        TEST_QUESTION.setAnswer("NEW_ANSWER");
        TEST_QUESTION.setCategory(POS_WIDGET_CATEGORY);

        mvc.perform(put(API_PATH + "/{id}", TEST_QUESTION.getId())
                   .contentType(CONTENT_TYPE)
                   .content(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION))
                   .accept(CONTENT_TYPE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.label").value(TEST_QUESTION.getLabel()))
           .andExpect(jsonPath("$.answer").value(TEST_QUESTION.getAnswer()))
           .andExpect(jsonPath("$.category.name").value(TEST_QUESTION.getCategory()
                                                                     .getName()));
    }

    @Test
    void testDeleteById() throws Exception {
        questionService.createQuestion(TEST_QUESTION);
        var id = TEST_QUESTION.getId();

        mvc.perform(delete(API_PATH + "/{id}", id)
                   .contentType(CONTENT_TYPE)
                   .accept(CONTENT_TYPE))
           .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> questionService.findQuestionById(id));
    }
}
