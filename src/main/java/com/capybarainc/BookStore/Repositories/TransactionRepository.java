package com.capybarainc.BookStore.Repositories;

import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    public List<Transaction> findByUser(User user);
}