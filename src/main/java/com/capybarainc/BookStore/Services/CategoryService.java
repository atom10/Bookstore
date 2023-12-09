package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Models.Category;
import com.capybarainc.BookStore.Repositories.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Category patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(category, JsonNode.class));
        return categoryRepository.save(objectMapper.treeToValue(patched, Category.class));
    }
}
