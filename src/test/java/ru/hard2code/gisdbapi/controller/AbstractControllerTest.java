package ru.hard2code.gisdbapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.hard2code.gisdbapi.testcontainers.CustomPostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractControllerTest {

    protected static final ObjectMapper objectMapper = new ObjectMapper();
    protected static final String CONTENT_TYPE =
            "application/json;charset=UTF-8";

    @Autowired
    protected MockMvc mvc;

    protected static CustomPostgreSQLContainer CONTAINER;


    protected static void init() {
        System.out.println("--INIT--");
        CONTAINER = CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
        configureMigrations();
    }


    protected static void shutdown() {
        System.out.println("--SHUTDOWN--");
        CONTAINER.stop();
        CONTAINER = null;
    }

    private static void configureMigrations() {
        Flyway.configure()
                .dataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(),
                        CONTAINER.getPassword())
                .locations("classpath:/db/migration")
                .load()
                .migrate();
    }

}
