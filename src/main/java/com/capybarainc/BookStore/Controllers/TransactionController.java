package com.capybarainc.BookStore.Controllers;

import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Repositories.TransactionRepository;
import com.capybarainc.BookStore.Services.TransactionService;
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
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    TransactionService cransactionService;
    @Autowired
    TransactionRepository cransactionRepository;

    @GetMapping("/")
    public List<Transaction> Get() {
        return cransactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Transaction GetOne(@PathVariable long id) {
        Optional<Transaction> Transaction = cransactionRepository.findById(id);
        if(!Transaction.isPresent()) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return Transaction.get();
    }

    @PostMapping("")
    public Transaction Post(@RequestBody Transaction Transaction) {
        cransactionRepository.save(Transaction);
        return Transaction;
    }

    @PutMapping("/{id}")
    public Transaction Put(@PathVariable("id") Long id, @RequestBody Transaction Transaction) {
        if(cransactionRepository.findById(id).isPresent()) {
            Transaction.setId(id);
            cransactionRepository.save(Transaction);
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
            Optional<Transaction> transaction = cransactionRepository.findById(id);
        if(transaction.isPresent()) {
            cransactionService.patch(id, jsonPatch);
            return transaction.get();
        } else {
            return Transaction.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        cransactionRepository.deleteById(id);
        response.setStatus(204);
    }
}
