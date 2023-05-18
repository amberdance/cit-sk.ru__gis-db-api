package ru.hard2code.gisdbapi.service.question;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.CategoryRepository;
import ru.hard2code.gisdbapi.repository.QuestionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = QuestionService.CACHE_NAME)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;


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
        if (question.getCategory().getId() != null)
            question.setCategory(categoryRepository.findById(question.getCategory().getId())
                                                   .orElseThrow(() -> new EntityNotFoundException(Category.class, question.getCategory().getId())));
        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question updateQuestion(long id, Question question) {
        var q = questionRepository.findById(id)
                                  .orElseGet(() -> questionRepository.save(question));
        var category = question.getCategory();

        q.setLabel(question.getLabel());
        q.setAnswer(question.getAnswer());
        q.setCategory(categoryRepository.findById(category.getId())
                                        .orElseThrow(() -> new EntityNotFoundException(Category.class, id)));

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
