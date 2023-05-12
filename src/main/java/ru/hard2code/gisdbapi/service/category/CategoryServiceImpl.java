package ru.hard2code.gisdbapi.service.category;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Category;
import ru.hard2code.gisdbapi.repository.CategoryRepository;

import java.util.List;

@Service
@CacheConfig(cacheNames = CategoryService.CACHE_NAME)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    @Cacheable( key = "'" + CategoryService.CACHE_LIST_KEY + "'")
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Cacheable( key = "#id")
    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, id));
    }

    @Override
    @CacheEvict( key = "#category.id")
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @CacheEvict(key = "#id")
    public Category updateCategory(long id, Category category) {
        var cat = categoryRepository.findById(id)
                .orElseGet(() -> categoryRepository.save(category));

        cat.setName(category.getName());

        return categoryRepository.save(cat);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
