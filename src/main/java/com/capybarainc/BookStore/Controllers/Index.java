package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("/api")
public class Index {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;

    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/book")
    public String getbook() {
        String ans = new String();
        List<Book> allBooks = bookRepository.findAll();
        for (Book book: allBooks
             ) {
            ans += "<p>" + book.getTitle() + "</p>";
        }
        return ans;
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
