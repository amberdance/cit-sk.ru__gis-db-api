package ru.hard2code.gisdbapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class AbstractMockMvcRequestBuilder {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    protected static final String CONTENT_TYPE =
            "application/json;charset=UTF-8";

    @Autowired
    protected MockMvc mvc;

    protected ResultActions mockHttpGet(String path)
            throws Exception {
        return mvc.perform(get(path)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    protected ResultActions mockHttpGet(String path, Object... uriParams)
            throws Exception {

        return mvc.perform(get(path, uriParams)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    protected ResultActions mockHttpPatch(String path, Object body)
            throws Exception {
        return mvc.perform(patch(path)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    protected ResultActions mockHttpPost(String path, Object body)
            throws Exception {
        return mvc.perform(post(path)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    protected ResultActions mockHttpPut(String path, Long id, Object body)
            throws Exception {
        return mvc.perform(put(path, id)
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(body))
                        .accept(CONTENT_TYPE))
                .andExpect(status().isOk());
    }

    protected ResultActions mockHttpDelete(String path, Long id)
            throws Exception {
        return mvc.perform(delete(path, id)
                        .contentType(CONTENT_TYPE)
                        .accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());
    }
}
