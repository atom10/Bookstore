package potatoparoto.MovieStore.Controllers;

import potatoparoto.MovieStore.Models.TransactionElement;
import potatoparoto.MovieStore.Repositories.TransactionElementRepository;
import potatoparoto.MovieStore.Services.TransactionElementService;
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
@RequestMapping("transactionelement")
public class TransactionElementController {
    @Autowired
    TransactionElementService transactionElementService;
    @Autowired
    TransactionElementRepository transactionElementRepository;

    @GetMapping("/")
    public List<TransactionElement> Get() {
        return transactionElementRepository.findAll();
    }

    @GetMapping("/{id}")
    public TransactionElement GetOne(@PathVariable long id) {
        Optional<TransactionElement> TransactionElement = transactionElementRepository.findById(id);
        if(!TransactionElement.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return TransactionElement.get();
    }

    @PostMapping("")
    public TransactionElement Post(@RequestBody TransactionElement TransactionElement) {
        transactionElementRepository.save(TransactionElement);
        return TransactionElement;
    }

    @PutMapping("/{id}")
    public TransactionElement Put(@PathVariable("id") Long id, @RequestBody TransactionElement TransactionElement) {
        if(transactionElementRepository.findById(id).isPresent()) {
            TransactionElement.setId(id);
            transactionElementRepository.save(TransactionElement);
            return TransactionElement;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public TransactionElement Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<TransactionElement> transactionElement = transactionElementRepository.findById(id);
        if(transactionElement.isPresent()) {
            transactionElementService.patch(id, jsonPatch);
            return transactionElement.get();
        } else {
            return TransactionElement.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        transactionElementRepository.deleteById(id);
        response.setStatus(204);
    }
}
