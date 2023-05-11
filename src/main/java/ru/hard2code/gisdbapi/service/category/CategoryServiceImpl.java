package ru.hard2code.gisdbapi.service.category;

import org.springframework.stereotype.Service;
import ru.hard2code.gisdbapi.exception.EntityNotFoundException;
import ru.hard2code.gisdbapi.model.Category;
import ru.hard2code.gisdbapi.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category gis) {
        return categoryRepository.save(gis);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, id));
    }

    @Override
    public void delete(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category update(long id, Category category) {
        var is = categoryRepository.findById(id)
                .orElseGet(() -> categoryRepository.save(category));
        is.setName(category.getName());

        return categoryRepository.save(is);
    }

    @Override
    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }
}
