package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Category;
import com.capybarainc.BookStore.Repositories.CategoryRepository;
import com.capybarainc.BookStore.Services.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public List<Category> Get() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category GetOne(@PathVariable long id) {
        Optional<Category> Category = categoryRepository.findById(id);
        if(!Category.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return Category.get();
    }

    @PostMapping("")
    public Category Post(@RequestBody Category Category) {
        categoryRepository.save(Category);
        return Category;
    }

    @PutMapping("/{id}")
    public Category Put(@PathVariable("id") Long id, @RequestBody Category Category) {
        if(categoryRepository.findById(id).isPresent()) {
            Category.setId(id);
            categoryRepository.save(Category);
            return Category;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Category Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()) {
            categoryService.patch(id, jsonPatch);
            return category.get();
        } else {
            return Category.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        categoryRepository.deleteById(id);
        response.setStatus(204);
    }
}
