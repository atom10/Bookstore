package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/")
    public List<Book> getbook() {
        return bookRepository.findAll();
    }

    @GetMapping("/createplaceholders")
    public String createplaceholders() {
        try {
            bookService.CreatePlaceholders(5);
        } catch (Exception e) {
            return e.getCause().toString();
        }
        return "OK";
    }
}
