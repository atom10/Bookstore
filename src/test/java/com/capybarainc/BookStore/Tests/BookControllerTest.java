package com.capybarainc.BookStore.Tests;

import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.jayway.jsonpath.JsonPath;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import javax.net.ssl.SSLEngineResult;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookService bookService;

    @Test
    public void CreatePlaceholders() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book/createplaceholders").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("OK")));
    }

    @Test
    public void GetAllBooks() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/book/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void GetOneBook() throws Exception {
        Author author = Author.builder().build();
        authorRepository.save(author);
        Book book = Book.builder().title("test").author(author).releaseDate(LocalDate.of(2000, 1, 1)).build();
        bookRepository.save(book);
        mvc.perform(MockMvcRequestBuilders.get("/book/"+book.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void DeleteBook() throws Exception {
        Author author = Author.builder().build();
        authorRepository.save(author);
        Book book = Book.builder().title("test").author(author).releaseDate(LocalDate.of(2000, 1, 1)).build();
        bookRepository.save(book);
        mvc.perform(MockMvcRequestBuilders.get("/book/"+book.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
        mvc.perform(MockMvcRequestBuilders.delete("/book/"+book.getId().toString())).andExpect(status().is(204));
        mvc.perform(MockMvcRequestBuilders.get("/book/"+book.getId().toString()))
                .andExpect(status().is(404));
    }

    @Test
    public void PatchBook() throws Exception {
        Author author = Author.builder().build();
        authorRepository.save(author);
        Book book = Book.builder().title("test").author(author).releaseDate(LocalDate.of(2000, 1, 1)).build();
        bookRepository.save(book);
        String patch = "[{" +
                "\"op\":\"replace\"," +
                "\"path\":\"/title\"," +
                "\"value\":\"test2\"" +
                "}]";
        mvc.perform(MockMvcRequestBuilders.patch("/book/"+book.getId().toString()).contentType("application/json-path+json").content(patch).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
        book = bookRepository.findById(book.getId()).get();
        Assert.isTrue(book.getTitle().equals("test2"), "Check patch result failed, was: " + book.getTitle());
    }
}
