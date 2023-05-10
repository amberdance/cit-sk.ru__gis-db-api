package ru.hard2code.gisdbapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.informationSystem.InformationSystemService;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Cache test")
class QuestionServiceTest extends AbstractServiceTest {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private InformationSystemService informationSystemService;


    private static final int QUESTIONS_COUNT = 3;

    private final List<Question> QUESTIONS = new ArrayList<>();


    @BeforeEach
    void setUp() {
        createQuestions();
        questionService.findAllQuestions();
    }

    @Test
    @SuppressWarnings("unchecked")
    void whenFindAllQuestionsThenCacheShouldCreated() {
        assertEquals(QUESTIONS.size(),
                ((List<Question>) cacheManager.getCache(QuestionService.CACHE_VALUE)
                        .get(QuestionService.CACHE_LIST_KEY).get()).size());
    }

    @Test
    void whenDeleteQuestionByIdThenCacheWillEvict() {
        questionService.deleteQuestionById(QUESTIONS.get(0).getId());

        assertNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(QuestionService.CACHE_LIST_KEY));
    }

    @Test
    void whenCreateQuestionThenCacheWillEvict() {
        var cacheBefore = cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(QuestionService.CACHE_LIST_KEY);

        questionService.createQuestion(new Question("test", "test",
                informationSystemService.createInformationSystem(new InformationSystem("test"))));

        assertNotEquals(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(QuestionService.CACHE_LIST_KEY), cacheBefore);
    }

    @Test
    void whenFindQuestionByIdThenQuestionWillReturnedFromCache() {
        var id = QUESTIONS.get(0).getId();
        questionService.findQuestionById(id);

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE).get(id));
    }

    @Test
    void whenFindQuestionByInformationSystemIdThenQuestionsWillReturnedFromCache() {
        var informationSystem =
                informationSystemService.createInformationSystem(new InformationSystem("test"));

        questionService.createQuestion(new Question("test", "test", informationSystem));
        questionService.findQuestionsByInformationSystemId(informationSystem.getId());

        assertNotNull(cacheManager.getCache(QuestionService.CACHE_VALUE)
                .get(informationSystem.getId()));
    }

    private void createQuestions() {
        //Cascade deleting here
        informationSystemService.deleteAllInformationSystems();

        for (int i = 0; i < QuestionServiceTest.QUESTIONS_COUNT; i++) {
            var is = informationSystemService.createInformationSystem(new InformationSystem(String.valueOf(i)));
            var savedQuestion = questionService.createQuestion(new Question(String.valueOf(i), String.valueOf(i), is));

            QUESTIONS.add(savedQuestion);
        }
    }
}
