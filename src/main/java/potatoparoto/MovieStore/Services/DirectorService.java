package potatoparoto.MovieStore.Services;

import potatoparoto.MovieStore.Models.Director;
import potatoparoto.MovieStore.Repositories.DirectorRepository;
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
public class DirectorService {
    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Director patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Director director = directorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(director, JsonNode.class));
        return directorRepository.save(objectMapper.treeToValue(patched, Director.class));
    }
}
