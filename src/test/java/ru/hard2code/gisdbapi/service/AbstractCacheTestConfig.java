package ru.hard2code.gisdbapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import ru.hard2code.gisdbapi.testcontainers.CustomPostgreSQLContainer;

@SpringBootTest
public class AbstractCacheTestConfig {

    @Autowired
    protected CacheManager cacheManager;

    protected static final CustomPostgreSQLContainer CONTAINER =
            CustomPostgreSQLContainer.getInstance();

    protected void clearAllCache() {
        cacheManager.getCacheNames().parallelStream()
                .forEach(name -> cacheManager.getCache(name).clear());
    }

}
