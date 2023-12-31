package potatoparoto.MovieStore.Services;

import potatoparoto.MovieStore.Models.TransactionElement;
import potatoparoto.MovieStore.Repositories.TransactionElementRepository;
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
}
