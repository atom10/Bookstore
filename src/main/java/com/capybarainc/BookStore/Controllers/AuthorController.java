package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
import com.capybarainc.BookStore.Services.AuthorService;
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
@RequestMapping("author")
public class AuthorController {
    @Autowired
    AuthorService authorService;
    @Autowired
    AuthorRepository authorRepository;

    @GetMapping("/")
    public List<Author> Get() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Author GetOne(@PathVariable long id) {
        Optional<Author> Author = authorRepository.findById(id);
        if(!Author.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return Author.get();
    }

    @PostMapping("")
    public Author Post(@RequestBody Author Author) {
        authorRepository.save(Author);
        return Author;
    }

    @PutMapping("/{id}")
    public Author Put(@PathVariable("id") Long id, @RequestBody Author Author) {
        if(authorRepository.findById(id).isPresent()) {
            Author.setId(id);
            authorRepository.save(Author);
            return Author;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Author Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()) {
            authorService.patch(id, jsonPatch);
            return author.get();
        } else {
            return Author.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        authorRepository.deleteById(id);
        response.setStatus(204);
    }
}
