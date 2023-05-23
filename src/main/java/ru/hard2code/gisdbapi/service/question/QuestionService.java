package ru.hard2code.gisdbapi.service.question;

import ru.hard2code.gisdbapi.domain.entity.Question;

import java.util.List;

public interface QuestionService {
    String CACHE_NAME = "questions";
    String CACHE_LIST_KEY = "all";
    String CACHE_LIST_BY_CATEGORY_KEY = "list_by_category";

    List<Question> findAllQuestions();

    Question findQuestionById(long id);

    List<Question> findQuestionsByCategoryId(long id);

    Question createQuestion(Question question);

    Question updateQuestion(long id, Question question);

    void deleteQuestionById(long id);

    void deleteAllQuestions();
}
