package ru.hard2code.gisdbapi.service.question;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.repository.CategoryRepository;
import ru.hard2code.gisdbapi.repository.QuestionRepository;

import java.util.List;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = QuestionService.CACHE_VALUE, key = "'" + QuestionService.CACHE_LIST_KEY + "'")
    public List<Question> findAllQuestions() {
        log.info("Getting all questions");
        return questionRepository.findAll();

    }

    @Override
    @Cacheable(value = QuestionService.CACHE_VALUE, key = "#id")
    public Question findQuestionById(long id) {
        log.info("Getting question by id: {}", id);
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    @Cacheable(value = QuestionService.CACHE_VALUE, key = "#id")
    public List<Question> findQuestionsByCategoryId(long id) {
        return questionRepository.findByCategory_Id(id);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public Question updateQuestion(long id, Question question) {
        var q = questionRepository.findById(id)
                .orElseGet(() -> questionRepository.save(question));
        var is = question.getCategory();

        q.setLabel(question.getLabel());
        q.setAnswer(question.getAnswer());
        q.setCategory(categoryRepository.findById(is.getId())
                .orElseThrow(() -> new EntityNotFoundException(is)));

        log.info("Creating question {}:", q);
        return questionRepository.save(q);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public void deleteQuestionById(long id) {
        log.info("Deleting question by id: {}", id);
        questionRepository.deleteById(id);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public void deleteAllQuestions() {
        log.info("Deleting all questions");
        questionRepository.deleteAll();
    }
}
