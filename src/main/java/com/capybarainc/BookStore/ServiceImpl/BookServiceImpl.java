package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Override
    public void CreatePlaceholders(int howMany) {
        List<String> names = new LinkedList<String>();
        names.add("Teksańska masakra papugą ziemniaczaną");
        names.add("Pinokio i komnata tajemnic");
        names.add("Zbrodnia i Kolos");

        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Jan", "Brzytwa", "Jan Brzytwa"));
        authors.add(new Author("Jędzrzej", "Sapiący","Jędrzej Sapiący"));
        authors.add(new Author("Stefan", "Kink","Stafan Kink"));
        authorRepository.saveAll(authors);

        Random random = new Random();
        List<Book> books = new LinkedList<>();
        for(int a=0; a<howMany; ++a) {
            books.add(new Book(
                    names.get(random.nextInt(names.size())),
                    authors.get(random.nextInt(authors.size())),
                    LocalDate.of(random.nextInt(1980, 2023), 1, 1)
            ));
        }

        bookRepository.saveAll(books);
    }
}
