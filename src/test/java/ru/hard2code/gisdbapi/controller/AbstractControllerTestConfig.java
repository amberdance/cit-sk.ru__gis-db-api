package ru.hard2code.gisdbapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.hard2code.gisdbapi.testcontainers.CustomPostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@AutoConfigureMockMvc
@SpringBootTest
public class AbstractControllerTestConfig {

    protected static final CustomPostgreSQLContainer CONTAINER =
            CustomPostgreSQLContainer.getInstance();

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected static final String CONTENT_TYPE =
            "application/json;charset=UTF-8";


    @Autowired
    protected MockMvc mvc;

    protected ResultActions mockHttpGet(String path) {
        try {
            return mvc.perform(get(path)
                    .contentType(CONTENT_TYPE)
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions mockHttpGet(String path, Object... uriParams) {

        try {
            return mvc.perform(get(path, uriParams)
                    .contentType(CONTENT_TYPE)
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions mockHttpPost(String path, Object body) {
        try {
            return mvc.perform(post(path)
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body))
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions mockHttpPatch(String path, Long id, Object body) {
        try {
            return mvc.perform(patch(path, id)
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body))
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected ResultActions mockHttpPut(String path, Long id, Object body) {
        try {
            return mvc.perform(put(path, id)
                    .contentType(CONTENT_TYPE)
                    .content(objectMapper.writeValueAsString(body))
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ResultActions mockHttpDelete(String path, Long id) {
        try {
            return mvc.perform(delete(path, id)
                    .contentType(CONTENT_TYPE)
                    .accept(CONTENT_TYPE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
