package com.capybarainc.BookStore.Repositories;

import com.capybarainc.BookStore.Models.Book;
import com.capybarainc.BookStore.Models.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
}