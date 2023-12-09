package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.DTO.BookDTO;
import com.capybarainc.BookStore.DTO.TransactionElementDTO;
import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void CreatePlaceholders(int howMany) {
        List<String> names = new LinkedList<>();
        names.addAll(Arrays.asList(
                "Teksańska masakra papugą ziemniaczaną",
                "Pinokio i komnata tajemnic",
                "Zbrodnia i Kolos"
        ));

        List<Author> authors = new ArrayList<>();
        authors.addAll(Arrays.asList(
                Author.builder().firstName("Jan").lastName("Brzytwa").displayedName("Jan Brzytwa").build(),
                Author.builder().firstName("Jędzrzej").lastName("Sapiący").displayedName("Jędzrzej Sapiący").build(),
                Author.builder().firstName("Stefan").lastName("Kink").displayedName("Stefan Kink").build()
        ));
        authorRepository.saveAll(authors);

        Random random = new Random();
        List<Book> books = new LinkedList<>();
        for(int a=0; a<howMany; ++a) {
            books.add(Book.builder()
                .title(names.get(random.nextInt(names.size())))
                .author(authors.get(random.nextInt(authors.size())))
                .releaseDate(LocalDate.of(random.nextInt(1980, 2023), 1, 1))
                .build()
            );
        }

        bookRepository.saveAll(books);
    }

    public Book patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(book, JsonNode.class));
        return bookRepository.save(objectMapper.treeToValue(patched, Book.class));
    }

    public BookDTO mapToBookDTO(Book book) {
        return objectMapper.convertValue(book, BookDTO.class);
    }

    public Book mapToTransactionElemrnt(BookDTO bookDTO) {
        return objectMapper.convertValue(bookDTO, Book.class);
    }
}
