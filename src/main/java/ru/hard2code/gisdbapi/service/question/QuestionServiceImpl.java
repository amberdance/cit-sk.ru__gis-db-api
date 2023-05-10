package ru.hard2code.gisdbapi.service.question;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.repository.InformationSystemRepository;
import ru.hard2code.gisdbapi.repository.QuestionRepository;

import java.util.List;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final InformationSystemRepository informationSystemRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, InformationSystemRepository informationSystemRepository) {
        this.questionRepository = questionRepository;
        this.informationSystemRepository = informationSystemRepository;
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
        return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    @Cacheable(value = QuestionService.CACHE_VALUE, key = "#id")
    public List<Question> findQuestionsByInformationSystemId(long id) {
        return questionRepository.findByInformationSystem_Id(id);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    @CacheEvict(value = QuestionService.CACHE_VALUE, allEntries = true)
    public Question updateQuestion(long id, Question question) {
        var q = questionRepository.findById(id).orElseGet(() -> questionRepository.save(question));
        var is = question.getInformationSystem();

        q.setLabel(question.getLabel());
        q.setAnswer(question.getAnswer());
        q.setInformationSystem(informationSystemRepository.findById(is.getId()).orElseThrow(() -> new EntityNotFoundException(is)));

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
