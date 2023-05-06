package ru.hard2code.gisdbapi.service.question;

import ru.hard2code.gisdbapi.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> findAllQuestions();

    Question findQuestionById(long id);

    List<Question> findQuestionsByInformationSystemId(long id);

    Question createQuestion(Question question);

    Question updateQuestion(long id, Question question);

    void deleteQuestionById(long id);
}
