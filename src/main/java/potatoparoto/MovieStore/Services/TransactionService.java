package potatoparoto.MovieStore.Services;

import potatoparoto.MovieStore.Models.Transaction;
import potatoparoto.MovieStore.Models.TransactionElement;
import potatoparoto.MovieStore.Repositories.TransactionElementRepository;
import potatoparoto.MovieStore.Repositories.TransactionRepository;
import potatoparoto.MovieStore.Repositories.UserRepository;
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

    private List<TransactionElement> getTransactionElementsFromIds(List<Long> transactionElementIds) {
        return transactionElementIds.stream()
                .map(transactionElementRepository::getById)
                .collect(Collectors.toList());
    }
}
