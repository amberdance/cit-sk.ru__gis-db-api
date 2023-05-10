package ru.hard2code.gisdbapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractControllerTest {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Autowired
    protected MockMvc mvc;

}
