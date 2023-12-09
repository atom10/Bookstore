package com.capybarainc.BookStore.Repositories;

import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}