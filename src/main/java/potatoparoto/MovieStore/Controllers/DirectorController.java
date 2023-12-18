package potatoparoto.MovieStore.Controllers;

import potatoparoto.MovieStore.Models.Director;
import potatoparoto.MovieStore.Repositories.DirectorRepository;
import potatoparoto.MovieStore.Services.DirectorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("director")
public class DirectorController {
    @Autowired
    DirectorService directorService;
    @Autowired
    DirectorRepository directorRepository;

    @GetMapping("")
    public List<Director> Get() {
        return directorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Director GetOne(@PathVariable long id) {
        Optional<Director> Author = directorRepository.findById(id);
        if(!Author.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return Author.get();
    }

    @PostMapping("")
    public Director Post(@RequestBody Director Director) {
        directorRepository.save(Director);
        return Director;
    }

    @PutMapping("/{id}")
    public Director Put(@PathVariable("id") Long id, @RequestBody Director Director) {
        if(directorRepository.findById(id).isPresent()) {
            Director.setId(id);
            directorRepository.save(Director);
            return Director;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Director Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Director> author = directorRepository.findById(id);
        if(author.isPresent()) {
            directorService.patch(id, jsonPatch);
            return author.get();
        } else {
            return Director.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        directorRepository.deleteById(id);
        response.setStatus(204);
    }
}
