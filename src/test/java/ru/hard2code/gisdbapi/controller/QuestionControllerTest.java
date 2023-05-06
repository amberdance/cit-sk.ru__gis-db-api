package ru.hard2code.gisdbapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.hard2code.gisdbapi.model.InformationSystem;
import ru.hard2code.gisdbapi.model.Question;
import ru.hard2code.gisdbapi.service.question.QuestionService;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest extends ControllerTestConfig {

    @Autowired
    private QuestionService questionService;

    private final InformationSystem TEST_INFORMATION_SYSTEM = new InformationSystem("GOSWEB");

    private final Question TEST_QUESTION = new Question("q1", "a1", TEST_INFORMATION_SYSTEM);

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("delete from questions");
    }

    @Test
    void getAllQuestions() throws Exception {
        var q1 = new Question("q1", "1", TEST_INFORMATION_SYSTEM);
        var q2 = new Question("q2", "2", new InformationSystem("POSWIDGET"));

        questionService.createQuestion(q1);
        questionService.createQuestion(q2);

        mvc.perform(get("/questions")
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(List.of(q1, q2))));
    }

    @Test
    void getQuestionById() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(get("/questions/{id}", TEST_QUESTION.getId())
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().string(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION)));
    }

    @Test
    void createQuestion() throws Exception {
        questionService.createQuestion(TEST_QUESTION);

        mvc.perform(post("/questions")
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.informationSystem.id").value(notNullValue()));
    }

    @Test
    void updateQuestion() throws Exception {
        questionService.createQuestion(TEST_QUESTION);
        TEST_QUESTION.setLabel("NEW_LABEL");
        TEST_QUESTION.setAnswer("NEW_ANSWER");
        TEST_QUESTION.setInformationSystem(new InformationSystem("NEW_IS"));

        mvc.perform(put("/questions/{id}", TEST_QUESTION.getId())
                        .contentType(CONTENT_TYPE)
                        .content(OBJECT_MAPPER.writeValueAsString(TEST_QUESTION))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    @Test
    void deleteQuestion() throws Exception {
        questionService.createQuestion(TEST_QUESTION);
        var id = TEST_QUESTION.getId();

        mvc.perform(delete("/questions/{id}", id)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());

        assertThrows(EntityNotFoundException.class, () -> questionService.findQuestionById(id));
    }
}