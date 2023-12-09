package com.capybarainc.BookStore.Services;

import com.capybarainc.BookStore.DTO.AuthorDTO;
import com.capybarainc.BookStore.DTO.AuthorDTO;
import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Models.Author;
import com.capybarainc.BookStore.Repositories.AuthorRepository;
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
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Author patch(Long id, JsonPatch jsonPatch) throws EntityNotFoundException, JsonProcessingException, JsonPatchException {
        Author author = authorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(author, JsonNode.class));
        return authorRepository.save(objectMapper.treeToValue(patched, Author.class));
    }

    public AuthorDTO mapToAuthorDTO(Author author) {
        return objectMapper.convertValue(author, AuthorDTO.class);
    }

    public Author mapToTransactionElemrnt(AuthorDTO authorDTO) {
        return objectMapper.convertValue(authorDTO, Author.class);
    }
}
