package ru.hard2code.GisDatabaseApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

public class ControllerTestConfig {
    protected final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    protected final static String CONTENT_TYPE = "application/json;charset=UTF-8";
    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
