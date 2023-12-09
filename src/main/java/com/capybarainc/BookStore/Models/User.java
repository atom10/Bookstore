package com.capybarainc.BookStore.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "Users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length=63, nullable=false, unique=true)
    private String login;

    @Column(length=127, nullable=false, unique=true)
    private String email;

    @Column(length=511, nullable=false)
    private String password;

    @OneToMany
    private List<Book> favouriteAuthors;

    @Column(columnDefinition = "DATE")
    private LocalDate birthDate;

    private String salt;
}
