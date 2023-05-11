package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hard2code.gisdbapi.model.Category;
import ru.hard2code.gisdbapi.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "CategoryController", description = "Question categories management API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category gis) {
        return categoryService.createCategory(gis);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("{id}")
    Category getCategoryById(@PathVariable("id") long id) {
        return categoryService.findById(id);
    }

    @PutMapping("{id}")
    Category updateCategory(@PathVariable("id") long id, @RequestBody Category category) {
        return categoryService.update(id, category);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.delete(id);
    }

}
