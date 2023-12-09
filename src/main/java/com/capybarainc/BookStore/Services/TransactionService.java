package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Transaction patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(transaction, JsonNode.class));
        return transactionRepository.save(objectMapper.treeToValue(patched, Transaction.class));
    }
}
