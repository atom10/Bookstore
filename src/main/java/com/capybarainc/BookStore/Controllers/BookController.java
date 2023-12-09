package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/")
    public List<Book> Get() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book GetOne(@PathVariable long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(!book.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return book.get();
    }

    @PostMapping("")
    public Book Post(@RequestBody Book book) {
        String bearer = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        System.out.println(bearer);
        bookRepository.save(book);
        return book;
    }

    @PutMapping("/{id}")
    public Book Put(@PathVariable("id") Long id, @RequestBody Book book) {
        if(bookRepository.findById(id).isPresent()) {
            book.setId(id);
            bookRepository.save(book);
            return book;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Book Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            bookService.patch(id, jsonPatch);
            return book.get();
        } else {
            return Book.builder().build();
        }
    }

    @GetMapping("/createplaceholders")
    public void CreatePlaceholders(HttpServletResponse response) {
        try {
            bookService.CreatePlaceholders(5);
        } catch (Exception e) {
            throw e;
        }
        response.setStatus(200);
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        bookRepository.deleteById(id);
        response.setStatus(204);
    }
}
