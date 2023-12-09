package com.capybarainc.BookStore.Repositories;

import com.capybarainc.BookStore.Models.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, Long> {
    public List<Author> findAll();
}