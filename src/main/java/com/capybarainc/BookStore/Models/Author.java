package com.capybarainc.BookStore.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "Authors")
@NoArgsConstructor
public class Author {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String displayedName;

    public Author(String firstName, String lastName, String displayedName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayedName = displayedName;
    }
}
