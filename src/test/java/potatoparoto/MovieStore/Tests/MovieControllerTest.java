package potatoparoto.MovieStore.Tests;

import potatoparoto.MovieStore.Models.Director;
import potatoparoto.MovieStore.Models.Movie;
import potatoparoto.MovieStore.Repositories.DirectorRepository;
import potatoparoto.MovieStore.Repositories.MovieRepository;
import potatoparoto.MovieStore.Services.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    MovieService movieService;

    @Test
    public void CreatePlaceholders() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/movie/createplaceholders").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("OK")));
    }

    @Test
    public void GetAllBooks() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/movie/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void GetOneBook() throws Exception {
        Director director = Director.builder().build();
        directorRepository.save(director);
        Movie movie = Movie.builder().title("test").director(director).releaseDate(LocalDate.of(2000, 1, 1)).build();
        movieRepository.save(movie);
        mvc.perform(MockMvcRequestBuilders.get("/movie/"+ movie.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
    }

    @Test
    public void DeleteBook() throws Exception {
        Director director = Director.builder().build();
        directorRepository.save(director);
        Movie movie = Movie.builder().title("movie").director(director).releaseDate(LocalDate.of(2000, 1, 1)).build();
        movieRepository.save(movie);
        mvc.perform(MockMvcRequestBuilders.get("/movie/"+ movie.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
        mvc.perform(MockMvcRequestBuilders.delete("/movie/"+ movie.getId().toString())).andExpect(status().is(204));
        mvc.perform(MockMvcRequestBuilders.get("/movie/"+ movie.getId().toString()))
                .andExpect(status().is(404));
    }

    @Test
    public void PatchBook() throws Exception {
        Director director = Director.builder().build();
        directorRepository.save(director);
        Movie movie = Movie.builder().title("test").director(director).releaseDate(LocalDate.of(2000, 1, 1)).build();
        movieRepository.save(movie);
        String patch = "[{" +
                "\"op\":\"replace\"," +
                "\"path\":\"/title\"," +
                "\"value\":\"test2\"" +
                "}]";
        mvc.perform(MockMvcRequestBuilders.patch("/movie/"+ movie.getId().toString()).contentType("application/json-path+json").content(patch).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(notNullValue()));
        movie = movieRepository.findById(movie.getId()).get();
        Assert.isTrue(movie.getTitle().equals("test2"), "Check patch result failed, was: " + movie.getTitle());
    }
}
