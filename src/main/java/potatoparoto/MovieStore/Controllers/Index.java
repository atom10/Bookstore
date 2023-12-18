package potatoparoto.MovieStore.Controllers;

import potatoparoto.MovieStore.Repositories.MovieRepository;
import potatoparoto.MovieStore.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api")
public class Index {
    @Autowired
    MovieService movieService;
    @Autowired
    MovieRepository movieRepository;

    @ResponseBody
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/jwt")
    public ResponseEntity<String> someClassNmae(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok("{\"token\": \"" + bearerToken + "\"}");
    }
}
