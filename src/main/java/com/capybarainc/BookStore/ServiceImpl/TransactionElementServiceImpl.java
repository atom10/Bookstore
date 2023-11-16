package com.capybarainc.BookStore.ServiceImpl;

import com.capybarainc.BookStore.Repositories.TransactionElementRepository;
import com.capybarainc.BookStore.Services.TransactionElementService;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionElementServiceImpl implements TransactionElementService {
    @Autowired
    TransactionElementRepository transactionElementRepository;
}
