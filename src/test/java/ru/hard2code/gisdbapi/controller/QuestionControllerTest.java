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

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(authorities = {"write", "read"})
class QuestionControllerTest extends AbstractTestContainersControllerTest {

    private static final String API_PATH = "/api" + Constants.Route.QUESTIONS;


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    @BeforeAll
    static void setup() {
        startContainer();
    }

    Question createRandomQuestion() {
        var random = RandomStringUtils.random(32);
        return new Question(null, random, random, new Category(random));
    }


    @Test
    void testFindAll() throws Exception {
        var questions = questionService.findAllQuestions();
        mockHttpGet(API_PATH).andExpect(
                content().string(objectMapper.writeValueAsString(questions)));
    }

    @Test
    void testFindById() throws Exception {
        var question = questionService.findQuestionById(1L);

        mockHttpGet(API_PATH + "/{id}", 1L).andExpect(content().string(
                objectMapper.writeValueAsString(question)));
    }

    @Test
    void testCreate() throws Exception {
        var question = createRandomQuestion();
        mockHttpPost(API_PATH, question)
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.category.id").value(notNullValue()));
    }

    @Test
    void whenPassedExistingIdInPOST_ThenOrganizationShouldBeCreatedInsteadOfUpdate()
            throws Exception {
        var existingQuestion = questionService.findQuestionById(1L);
        var anotherQuestion = Question.builder()
                .id(existingQuestion.getId())
                .label(RandomStringUtils.random(12))
                .answer((RandomStringUtils.random(1024)))
                .category(new Category((RandomStringUtils.random(12))))
                .build();

        mvc.perform(post(API_PATH)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(anotherQuestion))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id").value(not(existingQuestion.getId())));
    }

    @Test
    void testUpdate() throws Exception {
        var question = questionService.findQuestionById(1L);

        question.setLabel("NEW_LABEL");
        question.setAnswer("NEW_ANSWER");
        question.setCategory(categoryService.findById(1L));

        mockHttpPut(API_PATH + "/{id}", question.getId(), question)
                .andExpect(jsonPath("$.label").value("NEW_LABEL"))
                .andExpect(jsonPath("$.answer").value("NEW_ANSWER"))
                .andExpect(
                        jsonPath("$.category").value(question.getCategory()));
    }

    @Test
    void testDeleteById() throws Exception {
        var question = createRandomQuestion();
        questionService.createQuestion(question);

        mockHttpDelete(API_PATH + "/{id}", question.getId());
        assertThrows(EntityNotFoundException.class,
                () -> questionService.findQuestionById(question.getId()));
    }
}
