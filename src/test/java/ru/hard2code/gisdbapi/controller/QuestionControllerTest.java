package ru.hard2code.gisdbapi.controller;


import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.HashMap;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class QuestionControllerTest extends
        AbstractControllerTestConfig {

    private static final String API_PATH = "/api" + Constants.Route.QUESTIONS;


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    Question createRandomQuestion() {
        var random = RandomStringUtils.randomAlphabetic(12);
        return new Question(null, random, random, new Category(random));
    }


    @Test
    void testFindAll() throws Exception {
        var questions = questionService.findAllQuestions();

        mockHttpGet(API_PATH)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(questions)));
    }

    @Test
    void testFindById() throws Exception {
        var question = questionService.findQuestionById(1);

        mockHttpGet(API_PATH + "/{id}", 1)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(question)));
    }

    @Test
    void testCreate() throws Exception {
        var question = createRandomQuestion();

        mockHttpPost(API_PATH, question)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()));
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {

        var random = RandomStringUtils.randomAlphabetic(12);
        var existingQuestion = questionService.findQuestionById(1);
        var anotherQuestion = Question.builder()
                .id(existingQuestion.getId())
                .label(random)
                .answer(random)
                .category(new Category(random))
                .build();

        mockHttpPost(API_PATH, anotherQuestion)
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(not(existingQuestion.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var question = questionService.findQuestionById(2);

        question.setLabel("NEW_LABEL");
        question.setAnswer("NEW_ANSWER");
        question.setCategory(categoryService.findCategoryById(3));

        var request = new HashMap<>() {{
            put("label", question.getLabel());
            put("answer", question.getAnswer());
            put("category", question.getCategory());
        }};

        mockHttpPut(API_PATH + "/{id}", question.getId(), request)
                .andExpect(status().isOk())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(question)));

    }

    @Test
    void testDeleteById() throws Exception {
        var question = createRandomQuestion();
        questionService.createQuestion(question);

        mockHttpDelete(API_PATH + "/{id}", question.getId()).andExpect(
                status().isNoContent());

        assertThrows(EntityNotFoundException.class,
                () -> questionService.findQuestionById(question.getId()));
    }
}
