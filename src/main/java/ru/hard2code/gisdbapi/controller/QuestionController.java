package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.constants.Route;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

@RestController
@RequestMapping(Route.QUESTIONS)
@Tag(name = "QuestionController", description = "Questions management API")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions(@RequestParam(value = "categoryId",
            required = false) Long categoryId) {
        return categoryId == null ?
                questionService.findAllQuestions() :
                questionService.findQuestionsByCategoryId(categoryId);
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
