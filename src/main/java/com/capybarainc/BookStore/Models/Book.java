package com.capybarainc.BookStore.Models;

// Importing required classes
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String Title;
    private String Author;
    private String ReleaseDate;

    public Book(String title, String author, String releaseDate) {
        Title = title;
        Author = author;
        ReleaseDate = releaseDate;
    }
}
