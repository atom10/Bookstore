package potatoparoto.MovieStore.Services;

import potatoparoto.MovieStore.Models.Director;
import potatoparoto.MovieStore.Models.Movie;
import potatoparoto.MovieStore.Repositories.DirectorRepository;
import potatoparoto.MovieStore.Repositories.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void CreatePlaceholders(int howMany) {
        List<String> names = new LinkedList<>();
        names.addAll(Arrays.asList(
                "Szklana pułapka",
                "Top Gun: Maverick",
                "W pustyni i w puszczy"
        ));

        List<Director> directors = new ArrayList<>();
        directors.addAll(Arrays.asList(
                Director.builder().firstname("Jan").lastname("Brzytwa").displayedName("Jan Brzytwa").build(),
                Director.builder().firstname("Jędzrzej").lastname("Sapiący").displayedName("Jędzrzej Sapiący").build(),
                Director.builder().firstname("Stefan").lastname("Kink").displayedName("Stefan Kink").build()
        ));
        directorRepository.saveAll(directors);

        Random random = new Random();
        List<Movie> movies = new LinkedList<>();
        for(int a=0; a<howMany; ++a) {
            movies.add(Movie.builder()
                .title(names.get(random.nextInt(names.size())))
                .director(directors.get(random.nextInt(directors.size())))
                .releaseDate(LocalDate.of(random.nextInt(1980, 2023), 1, 1))
                .build()
            );
        }

        movieRepository.saveAll(movies);
    }

    public Movie patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Movie movie = movieRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(movie, JsonNode.class));
        return movieRepository.save(objectMapper.treeToValue(patched, Movie.class));
    }
}
