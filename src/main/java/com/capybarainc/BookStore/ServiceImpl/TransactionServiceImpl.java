package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Repositories.TransactionRepository;
import com.capybarainc.BookStore.Services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
}
