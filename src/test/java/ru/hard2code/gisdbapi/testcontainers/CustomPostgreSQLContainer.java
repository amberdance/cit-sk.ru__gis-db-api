package ru.hard2code.gisdbapi.testcontainers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPostgreSQLContainer
        extends PostgreSQLContainer<CustomPostgreSQLContainer> {

    private static final Logger logger =
            LoggerFactory.getLogger(CustomPostgreSQLContainer.class);

    private static final String IMAGE_VERSION = "postgres:13";

    private static CustomPostgreSQLContainer container;


    public CustomPostgreSQLContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPostgreSQLContainer getInstance() {
        if (container == null) {
            container = new CustomPostgreSQLContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        var url = container.getJdbcUrl() + "&stringtype=unspecified";

        System.setProperty("DB_URL", url);
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());

        logger.info("Postgres in docker started: url={}", url);
    }

    @Override
    public void stop() {
        super.stop();
    }


}
