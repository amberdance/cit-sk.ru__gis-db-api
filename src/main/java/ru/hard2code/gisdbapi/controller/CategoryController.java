package ru.hard2code.gisdbapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.hard2code.gisdbapi.domain.entity.Category;
import ru.hard2code.gisdbapi.service.category.CategoryService;
import ru.hard2code.gisdbapi.system.Constants;

import java.util.List;

@RestController
@RequestMapping(Constants.Route.CATEGORIES)
@Tag(name = "CategoryController", description = "Categories management API")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @PostMapping
    @Hidden
    public Category createCategory(@RequestBody Category gis) {
        return categoryService.createCategory(gis);
    }

    @GetMapping("{id}")
    Category getCategoryById(@PathVariable("id") long id) {
        return categoryService.findById(id);
    }

    @PutMapping("{id}")
    @Hidden
    Category updateCategory(@PathVariable("id") long id,
                            @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("{id}")
    @Hidden
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategoryById(id);
    }

}
