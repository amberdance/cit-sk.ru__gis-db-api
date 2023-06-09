package ru.hard2code.gisdbapi.service.question;


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
    public Question findQuestionById(long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    @Cacheable(key = "'" + QuestionService.CACHE_LIST_BY_CATEGORY_KEY +
            "_'+#id")
    public List<Question> findQuestionsByCategoryId(long id) {
        return questionRepository.findByCategory_Id(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question createQuestion(Question question) {
        question.setId(null);

        if (question.getCategory().getId() != null &&
                question.getCategory().getId() != 0) {
            question.setCategory(
                    categoryService.findCategoryById(
                            question.getCategory().getId()));
        }

        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Question updateQuestion(long id, Question q) {
        var optional = questionRepository.findById(id);

        if (optional.isEmpty()) {
            return createQuestion(q);
        }

        var question = optional.get().toBuilder()
                .answer(q.getAnswer())
                .category(q.getCategory())
                .label(q.getLabel())
                .build();

        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteQuestionById(long id) {
        questionRepository.deleteById(id);
    }

}
