package ru.hard2code.gisdbapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class QuestionServiceTest extends AbstractServiceTest<Question> {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private CategoryService categoryService;


    private void createInstances() {
        //Cascade deleting here
        categoryService.deleteAllCategories();

        for (int i = 0; i < INSTANCES_COUNT; i++) {
            var category = categoryService.createCategory(new Category(String.valueOf(i)));
            var savedQuestion = questionService.createQuestion(new Question(null, String.valueOf(i),
                    String.valueOf(i), category
            ));

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
                ((List<Question>) cacheManager.getCache(QuestionService.CACHE_NAME).get(QuestionService.CACHE_LIST_KEY).get()).size()
        );
    }

    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.deleteQuestionById(INSTANCES.get(0).getId());
        assertNull(cacheManager.getCache(QuestionService.CACHE_NAME).get(QuestionService.CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        var cacheBefore = cacheManager.getCache(QuestionService.CACHE_NAME).get(QuestionService.CACHE_LIST_KEY);

        questionService.createQuestion(new Question(null, "test", "test",
                categoryService.createCategory(new Category("test"))
        ));

        assertNotEquals(cacheManager.getCache(QuestionService.CACHE_NAME).get(QuestionService.CACHE_LIST_KEY),
                cacheBefore
        );
    }

    @Test
    void whenFindQuestionByIdThenQuestionWillReturnedFromCache() {
        var id = INSTANCES.get(0).getId();
        questionService.findQuestionById(id);

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_NAME).get(id));
    }

    @Test
    void whenFindQuestionByCategorySystemIdThenQuestionsWillReturnedFromCache() {
        var categoryId = categoryService.createCategory(new Category("test"));

        questionService.createQuestion(new Question(null, "test", "test", categoryId));
        questionService.findQuestionsByCategoryId(categoryId.getId());

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_NAME).get(categoryId.getId()));
    }

}
