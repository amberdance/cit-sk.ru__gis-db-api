package ru.hard2code.gisdbapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.model.Category;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Cache test")
class QuestionServiceTest extends AbstractServiceTest<Question> {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    private void createInstances() {
        //Cascade deleting here
        categoryService.deleteAllCategories();

        for (int i = 0; i < INSTANCES_COUNT; i++) {
            var is = categoryService.createCategory(new Category(String.valueOf(i)));
            var savedQuestion = questionService.createQuestion(new Question(String.valueOf(i), String.valueOf(i), is));

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
                ((List<Question>) cacheManager.getCache(QuestionService.CACHE_VALUE)
                        .get(QuestionService.CACHE_LIST_KEY).get()).size());
    }

    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.deleteQuestionById(INSTANCES.get(0).getId());

        assertNull(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(QuestionService.CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        var cacheBefore = cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(QuestionService.CACHE_LIST_KEY);

        questionService.createQuestion(new Question("test", "test",
                categoryService.createCategory(new Category("test"))));

        assertNotEquals(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(QuestionService.CACHE_LIST_KEY), cacheBefore);
    }

    @Test
    void whenFindQuestionByIdThenQuestionWillReturnedFromCache() {
        var id = INSTANCES.get(0).getId();
        questionService.findQuestionById(id);

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(id));
    }

    @Test
    void whenFindQuestionByCategorySystemIdThenQuestionsWillReturnedFromCache() {
        var categoryId =
                categoryService.createCategory(new Category("test"));

        questionService.createQuestion(new Question("test", "test", categoryId));
        questionService.findQuestionsByCategoryId(categoryId.getId());

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(categoryId.getId()));
    }

}
