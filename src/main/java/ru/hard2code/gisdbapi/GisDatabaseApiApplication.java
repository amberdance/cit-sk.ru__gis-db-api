package ru.hard2code.gisdbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GisDatabaseApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GisDatabaseApiApplication.class, args);
    }


}
