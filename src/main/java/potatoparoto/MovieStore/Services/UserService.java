package potatoparoto.MovieStore.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import potatoparoto.MovieStore.Models.User;
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

@Service
@Transactional
public class UserService {
    public static final String ISSUER = "BookStore";
    @Autowired
    UserRepository userRepository;

    @Autowired
    DirectorService directorService;

    @Autowired
    private ObjectMapper objectMapper;

    public User patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));
        return userRepository.save(objectMapper.treeToValue(patched, User.class));
    }

    public Algorithm GetAlgorithm() {
        return Algorithm.HMAC256("secretPhrase");
    }

    public JWTVerifier GetVerifier() {
        return JWT.require(GetAlgorithm())
                .withIssuer(ISSUER)
                .build();
    }
}
