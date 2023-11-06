package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Services.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;
    @Override
    public void CreatePlaceholders(int howMany) {
        List<String> names = new LinkedList<String>();
        names.add("Teksańska masakra papugą ziemniaczaną");
        names.add("Pinokio i komnata tajemnic");
        names.add("Zbrodnia i Kolos");

        List<String> authors = new LinkedList<String>();
        authors.add("Jan Brzytwa");
        authors.add("Jędrzej Sapiący");
        authors.add("Stafan Kink");

        Random random = new Random();
        List<Book> books = new LinkedList<Book>();
        for(int a=0; a<howMany; ++a) {
            books.add(new Book(
                    names.get(random.nextInt(names.size())),
                    authors.get(random.nextInt(authors.size())),
                    String.valueOf(random.nextInt(1980, 2023))
            ));
        }

        bookRepository.saveAll(books);
    }
}
