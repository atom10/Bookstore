package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Dictionary;
import java.util.Hashtable;
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

    @GetMapping("/jwt")
    public ResponseEntity<String> someClassNmae(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok("{\"token\": \"" + bearerToken + "\"}");
    }
}
