package ru.hard2code.gisdbapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
public class AbstractServiceTest {


    @Autowired
    protected CacheManager cacheManager;

}
