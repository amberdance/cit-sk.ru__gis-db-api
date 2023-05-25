package ru.hard2code.gisdbapi.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.repository.CategoryRepository;

import java.util.List;

@Service
@CacheConfig(cacheNames = CategoryService.CACHE_NAME)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    @Cacheable(key = "'" + CategoryService.CACHE_LIST_KEY + "'")
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(Category.class, id));
    }

    @Override
    @CacheEvict(allEntries = true)
    public Category createCategory(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Override
    @CacheEvict(allEntries = true)
    public Category updateCategory(long id, Category cat) {
        var optional = categoryRepository.findById(id);

        if (optional.isEmpty()) {
            return createCategory(cat);
        }

        var category = optional.get();
        category.setName(cat.getName());
        category.setQuestions(cat.getQuestions());

        return categoryRepository.save(category);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteCategoryById(long id) {
        categoryRepository.deleteById(id);
    }

}
