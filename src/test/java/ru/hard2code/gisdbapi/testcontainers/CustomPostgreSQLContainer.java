package ru.hard2code.gisdbapi.testcontainers;

import org.flywaydb.core.Flyway;
import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSQLContainer
        extends PostgreSQLContainer<CustomPostgreSQLContainer> {


    private static final String IMAGE_VERSION = "postgres:13";

    private static CustomPostgreSQLContainer container;


    private CustomPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CustomPostgreSQLContainer();
        }

        return container;
    }

    private static void configureMigrations() {
        Flyway.configure()
                .dataSource(container.getJdbcUrl(), container.getUsername(),
                        container.getPassword())
                .locations("classpath:/db/test-migration")
                .load()
                .migrate();
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        configureMigrations();
    }

    @Override
    public void stop() {
        super.stop();
    }


}
