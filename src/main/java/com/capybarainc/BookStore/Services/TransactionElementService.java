package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.DTO.TransactionDTO;
import com.capybarainc.BookStore.DTO.TransactionElementDTO;
import com.capybarainc.BookStore.DTO.UserDTO;
import com.capybarainc.BookStore.Models.Category;
import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.TransactionElement;
import com.capybarainc.BookStore.Models.User;
import com.capybarainc.BookStore.Repositories.TransactionElementRepository;
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
public class TransactionElementService {
    @Autowired
    TransactionElementRepository transactionElementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public TransactionElement patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        TransactionElement transactionElement = transactionElementRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(transactionElement, JsonNode.class));
        return transactionElementRepository.save(objectMapper.treeToValue(patched, TransactionElement.class));
    }

    public TransactionElementDTO mapToTransactionElementDTO(TransactionElement transactionElement) {
        return objectMapper.convertValue(transactionElement, TransactionElementDTO.class);
    }

    public TransactionElement mapToTransactionElemrnt(TransactionElementDTO transactionElementDTO) {
        return objectMapper.convertValue(transactionElementDTO, TransactionElement.class);
    }
}
