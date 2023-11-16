package com.capybarainc.BookStore.Models;

// Importing required classes
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Books")
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Nonnull
    private String title;
    @Nonnull
    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private Author author;
    @Nonnull
    @Column(columnDefinition = "DATE")
    private LocalDate releaseDate;
    @OneToMany
    private List<Category> categories;
    private Float price = 0f;

    public Book(String title, Author author, LocalDate releaseDate) {
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
    }
}
