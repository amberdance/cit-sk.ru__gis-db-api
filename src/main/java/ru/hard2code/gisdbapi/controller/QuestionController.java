package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.gisdbapi.domain.entity.Question;
import ru.hard2code.gisdbapi.service.question.QuestionService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.Route.QUESTIONS)
@Tag(name = "QuestionController", description = "Questions management API")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


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
    @Hidden
    public Question createQuestion(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @PutMapping("{id}")
    @Hidden
    public Question updateQuestion(@PathVariable("id") long id,
                                   @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Hidden
    public void deleteQuestion(@PathVariable("id") long id) {
        questionService.deleteQuestionById(id);
    }


}
