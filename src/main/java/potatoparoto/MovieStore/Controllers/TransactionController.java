package potatoparoto.MovieStore.Controllers;

import org.springframework.http.MediaType;
import potatoparoto.MovieStore.Methods.Verify;
import potatoparoto.MovieStore.Models.Transaction;
import potatoparoto.MovieStore.Models.TransactionElement;
import potatoparoto.MovieStore.Models.User;
import potatoparoto.MovieStore.Repositories.MovieRepository;
import potatoparoto.MovieStore.Repositories.TransactionElementRepository;
import potatoparoto.MovieStore.Repositories.TransactionRepository;
import potatoparoto.MovieStore.Repositories.UserRepository;
import potatoparoto.MovieStore.Services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    TransactionElementRepository transactionElementRepository;
    @Autowired
    Verify verify;

    @GetMapping("/")
    public List<Transaction> Get(@RequestHeader("Authorization") String bearerToken) {
        User user = userRepository.findByLogin(verify.GetLogin(bearerToken.replace("Bearer ",""))).getFirst();
        return transactionRepository.findByUser(user);
    }

    @GetMapping("/{id}")
    public Transaction GetOne(@PathVariable long id) {
        Optional<Transaction> Transaction = transactionRepository.findById(id);
        if(!Transaction.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return Transaction.get();
    }

    @PostMapping("")
    public Transaction Post(@RequestBody Transaction Transaction) {
        transactionRepository.save(Transaction);
        return Transaction;
    }

    @PutMapping("/{id}")
    public Transaction Put(@PathVariable("id") Long id, @RequestBody Transaction Transaction) {
        if(transactionRepository.findById(id).isPresent()) {
            Transaction.setId(id);
            transactionRepository.save(Transaction);
            return Transaction;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public Transaction Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<Transaction> transaction = transactionRepository.findById(id);
        if(transaction.isPresent()) {
            transactionService.patch(id, jsonPatch);
            return transaction.get();
        } else {
            return Transaction.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        transactionRepository.deleteById(id);
        response.setStatus(204);
    }

    @GetMapping("/getfromsession")
    public Transaction GetFromSession(@RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        transactionRepository.save(transaction);
        return transaction;
    }

    @PostMapping(value = "/addtotransaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transaction AddBookToTransaction(@RequestBody Map<String, String> formData, @RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        TransactionElement transactionElement = TransactionElement.builder().movie(movieRepository.getById(Long.parseLong(formData.get("movieId")))).quantity(Integer.parseInt(formData.get("quantity"))).build();
        transactionElementRepository.save(transactionElement);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        List<TransactionElement> transactionElements = transaction.getTransactionElements();
        transactionElements.add(transactionElement);
        transaction.setTransactionElements(transactionElements);
        transaction.setPrice(transaction.getPrice() + movieRepository.getById(Long.parseLong(formData.get("movieId"))).getPrice() * Integer.parseInt(formData.get("quantity")));
        transactionRepository.save(transaction);
        return transaction;
    }
    @DeleteMapping(value = "/removefromtransaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transaction RemoveFromTransaction(@RequestBody Map<String, String> formData, @RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        List<TransactionElement> transactionElements = transaction.getTransactionElements();
        TransactionElement transactionElement = transactionElements.stream().filter((TransactionElement te) -> te.getId() == Long.parseLong(formData.get("transactionElementId"))).findFirst().get();
        transaction.setPrice(transaction.getPrice() - transactionElement.getQuantity() * transactionElement.getMovie().getPrice());
        transactionElementRepository.delete(transactionElement);
        transactionElements.remove(transactionElement);
        transaction.setTransactionElements(transactionElements);
        transactionRepository.save(transaction);
        return transaction;
    }

    @GetMapping("/finalizetransaction")
    public Transaction FinalizeTransaction(@RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        transaction.setConfirmed(true);
        transactionRepository.save(transaction);
        return transaction;
    }
}
