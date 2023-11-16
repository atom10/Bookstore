package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Repositories.CategoryRepository;
import com.capybarainc.BookStore.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
}
