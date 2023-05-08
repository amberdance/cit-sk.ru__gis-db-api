package ru.hard2code.gisdbapi.service.question;


import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.repository.InformationSystemRepository;
import ru.hard2code.gisdbapi.repository.QuestionRepository;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final InformationSystemRepository informationSystemRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, InformationSystemRepository informationSystemRepository) {
        this.questionRepository = questionRepository;
        this.informationSystemRepository = informationSystemRepository;
    }

    @Override
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Question findQuestionById(long id) {
        return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Question.class, id));
    }

    @Override
    public List<Question> findQuestionsByInformationSystemId(long id) {
        return questionRepository.findByInformationSystem_Id(id);
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(long id, Question question) {
        var q = questionRepository.findById(id).orElseGet(() -> questionRepository.save(question));
        var is = question.getInformationSystem();

        q.setLabel(question.getLabel());
        q.setAnswer(question.getAnswer());
        q.setInformationSystem(informationSystemRepository.findById(is.getId()).orElseThrow(() -> new EntityNotFoundException(is)));

        return questionRepository.save(q);
    }

    @Override
    public void deleteQuestionById(long id) {
        questionRepository.deleteById(id);

    }
}
