package ru.hard2code.gisdbapi.controller;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.service.question.QuestionService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class QuestionControllerTest extends AbstractControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.QUESTIONS;
    private final Category GOS_WEB_CATEGORY = new Category("GOSWEB");
    private final Category POS_WIDGET_CATEGORY = new Category("POS_WIDGET");
    private final Question TEST_QUESTION =
            new Question(null, "q1", "a1", GOS_WEB_CATEGORY);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void startContainer() {
        init();
    }

    @AfterAll
    static void stopContainer() {
        shutdown();
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
                .andExpect(content().string(
                        objectMapper.writeValueAsString(List.of(q1, q2))));
    }

    @Test
    void testFindById() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(get(API_PATH + "/{id}", TEST_QUESTION.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(TEST_QUESTION)));
    }

    @Test
    void testCreate() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(TEST_QUESTION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.category.id").value(notNullValue()));
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        var anotherQuestion = Question.builder()
                .id(TEST_QUESTION.getId())
                .label("Another label")
                .answer("Answer")
                .category(new Category(null, "Name", Collections.emptySet()))
                .build();

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(anotherQuestion))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(not(TEST_QUESTION.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        questionService.createQuestion(TEST_QUESTION);
        TEST_QUESTION.setLabel("NEW_LABEL");
        TEST_QUESTION.setAnswer("NEW_ANSWER");
        TEST_QUESTION.setCategory(POS_WIDGET_CATEGORY);

        mvc.perform(put(API_PATH + "/{id}", TEST_QUESTION.getId())
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(TEST_QUESTION))
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
