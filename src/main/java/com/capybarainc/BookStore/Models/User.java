package com.capybarainc.BookStore.Models;

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
@Table(name = "Users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nonnull
    @Column(nullable=false, unique=true)
    private String login;

    @Nonnull
    @Column(nullable=false, unique=true)
    private String email;

    @Nonnull
    @Column(nullable=false)
    private String password;

    @OneToMany
    private List<Author> favouriteAuthors;

    @Column(columnDefinition = "DATE")
    private LocalDate birthDate;

    private String salt;
}
