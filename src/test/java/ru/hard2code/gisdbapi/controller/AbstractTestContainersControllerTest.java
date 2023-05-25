package ru.hard2code.gisdbapi.controller;

import org.flywaydb.core.Flyway;
import ru.hard2code.gisdbapi.testcontainers.CustomPostgreSQLContainer;


public class AbstractTestContainersControllerTest
        extends AbstractMockMvcRequestBuilder {

    protected static CustomPostgreSQLContainer CONTAINER;


    protected static void startContainer() {
        CONTAINER = CustomPostgreSQLContainer.getInstance().withReuse(true);
        CONTAINER.start();
        configureMigrations();
    }


    protected static void stopContainer() {
        CONTAINER.stop();
    }

    private static void configureMigrations() {
        Flyway.configure()
                .dataSource(CONTAINER.getJdbcUrl(), CONTAINER.getUsername(),
                        CONTAINER.getPassword())
                .locations("classpath:/db/test-migration")
                .load()
                .migrate();
    }

}
