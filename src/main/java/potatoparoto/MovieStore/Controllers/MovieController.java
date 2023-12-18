package potatoparoto.MovieStore.Controllers;

import potatoparoto.MovieStore.Models.Movie;
import potatoparoto.MovieStore.Repositories.MovieRepository;
import potatoparoto.MovieStore.Services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("movie")
public class MovieController {
    @Autowired
    MovieService movieService;
    @Autowired
    MovieRepository movieRepository;

    @GetMapping("/")
    public List<Movie> Get() {
        return movieRepository.findAll();
    }

    @GetMapping("/{id}")
    public Movie GetOne(@PathVariable long id) {
        Optional<Movie> book = movieRepository.findById(id);
        if(!book.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return book.get();
    }

    @PostMapping("")
    public Movie Post(@RequestBody Movie movie) {
        String bearer = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        System.out.println(bearer);
        movieRepository.save(movie);
        return movie;
    }

    @PutMapping("/{id}")
    public Movie Put(@PathVariable("id") Long id, @RequestBody Movie movie) {
        if(movieRepository.findById(id).isPresent()) {
            movie.setId(id);
            movieRepository.save(movie);
            return movie;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Movie Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Movie> book = movieRepository.findById(id);
        if(book.isPresent()) {
            movieService.patch(id, jsonPatch);
            return book.get();
        } else {
            return Movie.builder().build();
        }
    }

    @GetMapping("/createplaceholders")
    public String CreatePlaceholders(HttpServletResponse response) {
        try {
            movieService.CreatePlaceholders(5);
        } catch (Exception e) {
            throw e;
        }
        response.setStatus(200);
        return "OK";
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        movieRepository.deleteById(id);
        response.setStatus(204);
    }
}
