package ru.hard2code.gisdbapi.service.question;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.QuestionRepository;
import ru.hard2code.gisdbapi.service.category.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = QuestionService.CACHE_NAME)
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryService categoryService;


    @Override
    @Cacheable(key = "'" + QuestionService.CACHE_LIST_KEY + "'")
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();

    }

    @Override
    @Cacheable(key = "#id")
    public Question findQuestionById(long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    @Cacheable(key = "#id")
    public List<Question> findQuestionsByCategoryId(long id) {
        return questionRepository.findByCategory_Id(id);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Question createQuestion(Question question) {
        question.setId(null);

        if (question.getCategory().getId() != null &&
                question.getCategory().getId() != 0) {
            question.setCategory(
                    categoryService.findById(question.getCategory().getId()));
        }

        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question updateQuestion(long id, Question question) {

        var questionFromDb = questionRepository.findById(id)
                .orElseGet(() -> questionRepository.save(question))
                .toBuilder()
                .label(question.getLabel())
                .answer(question.getAnswer())
                .category(question.getCategory())
                .build();

        return questionRepository.save(questionFromDb);
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
