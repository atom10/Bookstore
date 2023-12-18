package potatoparoto.MovieStore.Models;

// Importing required classes
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Data
@Table(name = "Books")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Nonnull
    private String title;
    @Nonnull
    @ManyToOne
    private Director director;
    @Nonnull
    @Column(columnDefinition = "DATE")
    private LocalDate releaseDate;
    @OneToMany
    private List<Category> categories;
    private Float price = 0f;
}
