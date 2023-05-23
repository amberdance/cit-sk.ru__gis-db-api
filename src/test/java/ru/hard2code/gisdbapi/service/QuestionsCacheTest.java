package ru.hard2code.gisdbapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_LIST_BY_CATEGORY_KEY;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_LIST_KEY;
import static ru.hard2code.gisdbapi.service.question.QuestionService.CACHE_NAME;


class QuestionsCacheTest extends AbstractCacheTest<Question> {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    private void createInstances() {
        //Cascade deleting here
        categoryService.deleteAllCategories();

        for (int i = 0; i < INSTANCES_COUNT; i++) {
            var savedQuestion = questionService.createQuestion(
                    new Question(null, String.valueOf(i),
                            String.valueOf(i),
                            new Category(String.valueOf(i))));

            INSTANCES.add(savedQuestion);
        }
    }

    @BeforeEach
    void setUp() {
        createInstances();
        questionService.findAllQuestions();
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindAllQuestionsThenCacheShouldCreated() {
        assertEquals(INSTANCES.size(),
                ((List<Question>) cacheManager.getCache(
                                CACHE_NAME)
                        .get(CACHE_LIST_KEY).get()).size()
        );
    }

    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.deleteQuestionById(INSTANCES.get(0).getId());
        assertNull(cacheManager.getCache(CACHE_NAME)
                .get(CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        var cacheBefore = cacheManager.getCache(CACHE_NAME)
                .get(CACHE_LIST_KEY);

        questionService.createQuestion(
                new Question(null, "test", "test", new Category("test")));

        assertNotEquals(cacheManager.getCache(CACHE_NAME).get(CACHE_LIST_KEY),
                cacheBefore);
    }

    @Test
    void whenFindQuestionByIdThenQuestionWillReturnedFromCache() {
        var id = INSTANCES.get(0).getId();
        questionService.findQuestionById(id);

        assertNotNull(cacheManager.getCache(CACHE_NAME).get(id));
    }

    @Test
    void whenFindQuestionByCategoryThenQuestionsWillReturnedFromCache() {
        var question = questionService.createQuestion(new Question(null,
                "test", "test", new Category("test")));

        var categoryId = question.getCategory().getId();
        questionService.findQuestionsByCategoryId(categoryId);

        assertNotNull(cacheManager.getCache(CACHE_NAME)
                .get(CACHE_LIST_BY_CATEGORY_KEY + "_" + categoryId));
    }

}
