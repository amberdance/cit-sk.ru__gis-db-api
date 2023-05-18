package ru.hard2code.gisdbapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AbstractServiceTest<T> {


    @Autowired
    protected CacheManager cacheManager;

    protected static final int INSTANCES_COUNT = 3;

    protected final List<T> INSTANCES = new ArrayList<>();


}
