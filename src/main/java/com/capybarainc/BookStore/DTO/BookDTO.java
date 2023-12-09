package com.capybarainc.BookStore.DTO;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;

    @Nonnull
    private String title;

    @Nonnull
    private Long authorId;

    @Nonnull
    private String authorName;

    @Nonnull
    private LocalDate releaseDate;

    private List<Long> categoryIds;
    private Float price = 0f;
}