package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.DTO.TransactionDTO;
import com.capybarainc.BookStore.Methods.Verify;
import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Models.User;
import com.capybarainc.BookStore.Repositories.BookRepository;
import com.capybarainc.BookStore.Repositories.TransactionElementRepository;
import com.capybarainc.BookStore.Repositories.TransactionRepository;
import com.capybarainc.BookStore.Repositories.UserRepository;
import com.capybarainc.BookStore.Services.TransactionService;
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
    BookRepository bookRepository;
    @Autowired
    TransactionElementRepository transactionElementRepository;
    @Autowired
    Verify verify;

    @GetMapping("/")
    public List<Transaction> Get() {
        return transactionRepository.findAll();
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
    public TransactionDTO GetFromSession(@RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        transactionRepository.save(transaction);
        return transactionService.mapToTransactionDTO(transaction);
    }

    @PostMapping("/addtotransaction")
    public TransactionDTO AddBookToTransaction(Long bookId, Long quantity, @RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        TransactionElement transactionElement = TransactionElement.builder().book(bookRepository.getById(bookId)).quantity(quantity.intValue()).build();
        transactionElementRepository.save(transactionElement);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        List<TransactionElement> transactionElements = transaction.getTransactionElements();
        transactionElements.add(transactionElement);
        transaction.setTransactionElements(transactionElements);
        transactionRepository.save(transaction);
        return transactionService.mapToTransactionDTO(transaction);
    }

    @PostMapping("/removefromtransaction")
    public TransactionDTO RemoveFromTransaction(Long transactionElementId, @RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        List<TransactionElement> transactionElements = transaction.getTransactionElements();
        TransactionElement transactionElement = transactionElements.stream().filter((TransactionElement te) -> te.getId() == transactionElementId).findFirst().get();
        transactionElementRepository.delete(transactionElement);
        transactionElements.remove(transactionElement);
        transaction.setTransactionElements(transactionElements);
        transactionRepository.save(transaction);
        return transactionService.mapToTransactionDTO(transaction);
    }

    @GetMapping("/finalizetransaction")
    public TransactionDTO FinalizeTransaction(@RequestHeader("Authorization") String bearerToken) {
        String login = verify.GetLogin(bearerToken.replace("Bearer ",""));
        User user = userRepository.findByLogin(login).get(0);
        List<Transaction> transactions = transactionRepository.findByUser(user);
        Optional<Transaction> sessionTransaction = transactions.stream().filter((Transaction t) -> !t.getConfirmed()).findFirst();
        Transaction transaction = sessionTransaction.isPresent() ? sessionTransaction.get() : Transaction.builder().user(user).confirmed(false).transactionElements(new ArrayList<>()).build();
        transaction.setConfirmed(true);
        transactionRepository.save(transaction);
        return transactionService.mapToTransactionDTO(transaction);
    }
}
