package ru.hard2code.gisdbapi.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_LIST_BY_CATEGORY_KEY;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_LIST_KEY;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_NAME;

class QuestionsCacheTest extends AbstractCacheTestConfig {


    @Autowired
    private QuestionService questionService;

    @BeforeAll
    static void setup() {
        CONTAINER.start();
    }

    @AfterEach
    void cleanup() {
        clearAllCache();
    }

    private void createQuestion() {
        var random = RandomStringUtils.randomAlphabetic(12);
        var question = Question.builder()
                .label(random)
                .answer(random)
                .category(new Category(random))
                .build();

        questionService.createQuestion(question);
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindAllQuestionsThenCacheShouldCreated() {
        var questions = questionService.findAllQuestions();

        assertEquals(questions.size(),
                ((List<Question>) cacheManager.getCache(CACHE_NAME)
                        .get(CACHE_LIST_KEY).get()).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindQuestionByCategoryThenCacheShouldCreated() {
        var categoryId = 4;
        var questions = questionService.findQuestionsByCategoryId(categoryId);

        assertEquals(questions.size(),
                ((List<Question>) cacheManager.getCache(CACHE_NAME)
                        .get(CACHE_LIST_BY_CATEGORY_KEY + "_" + categoryId)
                        .get()).size());
    }


    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.findAllQuestions();
        assertNotNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));

        questionService.deleteQuestionById(6);
        assertNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        questionService.findAllQuestions();
        assertNotNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));

        createQuestion();
        assertNull(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY));
    }

}
