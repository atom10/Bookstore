package com.capybarainc.BookStore.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.capybarainc.BookStore.Models.Transaction;
import com.capybarainc.BookStore.Models.User;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
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

@Service
@Transactional
public class UserService {
    public static final String ISSUER = "BookStore";
    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public User patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));
        return userRepository.save(objectMapper.treeToValue(patched, User.class));
    }

    public Algorithm GetAlgorithm() {
        return Algorithm.HMAC256("baeldung");
    }

    public JWTVerifier GetVerifier() {
        return JWT.require(GetAlgorithm())
                .withIssuer("Baeldung")
                .build();
    }
}
