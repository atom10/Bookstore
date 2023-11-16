package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorServiceImpl {
    @Autowired
    AuthorRepository authorRepository;
}
