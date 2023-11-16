package com.capybarainc.BookStore.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "TransactionElements")
public class TransactionElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    @ManyToOne
    private Book book;
}
