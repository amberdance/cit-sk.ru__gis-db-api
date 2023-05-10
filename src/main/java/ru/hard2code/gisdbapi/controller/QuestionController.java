package ru.hard2code.gisdbapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions(@RequestParam(value = "informationSystemId", required = false) Long informationSystemId) {
        return informationSystemId == null ? questionService.findAllQuestions() : questionService.findQuestionsByInformationSystemId(informationSystemId);
    }

    @GetMapping("{id}")
    public Question getQuestionById(@PathVariable("id") long id) {
        return questionService.findQuestionById(id);
    }

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @PutMapping("{id}")
    public Question updateQuestion(@PathVariable("id") long id, @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable("id") long id) {
        questionService.deleteQuestionById(id);
    }


}
