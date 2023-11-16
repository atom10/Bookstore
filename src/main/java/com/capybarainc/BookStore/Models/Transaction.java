package com.capybarainc.BookStore.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany
    private List<TransactionElement> transactionElements;
    private Float price = 0f;
    private Float paid = 0f;
    private Boolean completed = false;
}
