package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.DTO.TransactionDTO;
import com.capybarainc.BookStore.DTO.UserDTO;
import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Models.User;
import com.capybarainc.BookStore.Repositories.TransactionElementRepository;
import com.capybarainc.BookStore.Repositories.TransactionRepository;
import com.capybarainc.BookStore.Repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionElementRepository transactionElementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Transaction patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(transaction, JsonNode.class));
        return transactionRepository.save(objectMapper.treeToValue(patched, Transaction.class));
    }

    private static List<Long> getTransactionElementIds(List<TransactionElement> transactionElements) {
        return transactionElements.stream()
                .map(TransactionElement::getId)
                .collect(Collectors.toList());
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
        //return objectMapper.convertValue(transaction, TransactionDTO.class);
        return TransactionDTO.builder()
                .id(transaction.getId())
                //.transactionElements(getTransactionElementIds(transaction.getTransactionElements()))
                .transactionElements(transaction.getTransactionElements())
                .price(transaction.getPrice())
                .userId(transaction.getUser().getId())
                .build();
    }

    public Transaction mapToTransaction(TransactionDTO transactionDTO) {
        //return objectMapper.convertValue(transactionDTO, Transaction.class);
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        //transaction.setTransactionElements(getTransactionElementsFromIds(transactionDTO.getTransactionElements()));
        transaction.setTransactionElements(transactionDTO.getTransactionElements());
        transaction.setPrice(transactionDTO.getPrice());
        transaction.setUser(userRepository.getById(transactionDTO.getUserId()));

        return transaction;
    }

    private List<TransactionElement> getTransactionElementsFromIds(List<Long> transactionElementIds) {
        return transactionElementIds.stream()
                .map(transactionElementRepository::getById)
                .collect(Collectors.toList());
    }
}
