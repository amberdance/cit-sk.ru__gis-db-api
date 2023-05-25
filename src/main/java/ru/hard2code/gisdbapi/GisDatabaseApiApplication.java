package ru.hard2code.gisdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class GisDatabaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GisDatabaseApiApplication.class, args);
    }


}
