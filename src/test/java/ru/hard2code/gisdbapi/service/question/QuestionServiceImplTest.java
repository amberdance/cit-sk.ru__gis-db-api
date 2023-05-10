package ru.hard2code.gisdbapi.service.question;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.informationSystem.InformationSystemService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
@DisplayName("Cache test")
class QuestionServiceImplTest {

    @SuppressWarnings("all")
    @Autowired
    private QuestionService questionService;

    @SuppressWarnings("all")
    @Autowired
    private InformationSystemService informationSystemService;

    @Autowired
    private CacheManager cacheManager;


    private static final int QUESTIONS_COUNT = 3;

    private final List<Question> QUESTIONS = new ArrayList<>();


    @BeforeEach
    void setUp() {
        createQuestions();
        questionService.findAllQuestions();
    }

    @Test
    void whenFindAllQuestionsThenCacheShouldCreated() {
        Assertions.assertEquals(QUESTIONS.size(), ((List<Question>) cacheManager.getCache(QuestionService.CACHE_VALUE).get(QuestionService.CACHE_LIST_KEY).get()).size());
    }

    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.deleteQuestionById(QUESTIONS.get(0).getId());
        assertNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(QuestionService.CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        // when
        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(QuestionService.CACHE_LIST_KEY));

        // then
        questionService.createQuestion(new Question("test", "test", informationSystemService.createInformationSystem(new InformationSystem("test"))));
        assertNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(QuestionService.CACHE_LIST_KEY));
    }

    @Test
    void whenFindQuestionByIdThenQuestionWillReturnedFromCache() {
        var id = QUESTIONS.get(0).getId();
        questionService.findQuestionById(id);
        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(id));
    }

    @Test
    void whenFindQuestionByInformationSystemIdThenQuestionsWillReturnedFromCache() {
        var informationSystem = informationSystemService.createInformationSystem(new InformationSystem("test"));
        questionService.createQuestion(new Question("test", "test", informationSystem));
        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(informationSystem.getId()));
    }

    private void createQuestions() {
        //Cascade deleting here
        informationSystemService.deleteAllInformationSystems();

        for (int i = 0; i < QuestionServiceImplTest.QUESTIONS_COUNT; i++) {
            var is = informationSystemService.createInformationSystem(new InformationSystem(String.valueOf(i)));
            var savedQuestion = questionService.createQuestion(new Question(String.valueOf(i), String.valueOf(i), is));

            QUESTIONS.add(savedQuestion);
        }
    }
}