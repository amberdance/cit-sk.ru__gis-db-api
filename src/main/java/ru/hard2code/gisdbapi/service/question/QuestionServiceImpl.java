package ru.hard2code.gisdbapi.service.question;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.repository.CategoryRepository;
import ru.hard2code.gisdbapi.repository.QuestionRepository;

import java.util.List;

@Service
@CacheConfig(cacheNames = QuestionService.CACHE_NAME)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(key = "'" + QuestionService.CACHE_LIST_KEY + "'")
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();

    }

    @Override
    @Cacheable(key = "#id")
    public Question findQuestionById(long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    @Cacheable(key = "#id")
    public List<Question> findQuestionsByCategoryId(long id) {
        return questionRepository.findByCategory_Id(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question updateQuestion(long id, Question question) {
        var q = questionRepository.findById(id)
                .orElseGet(() -> questionRepository.save(question));
        var is = question.getCategory();

        q.setLabel(question.getLabel());
        q.setAnswer(question.getAnswer());
        q.setCategory(categoryRepository.findById(is.getId())
                .orElseThrow(() -> new EntityNotFoundException(is)));

        return questionRepository.save(q);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteQuestionById(long id) {
        questionRepository.deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteAllQuestions() {
        questionRepository.deleteAll();
    }
}
