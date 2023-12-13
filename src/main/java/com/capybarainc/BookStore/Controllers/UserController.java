package com.capybarainc.BookStore.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.capybarainc.BookStore.DTO.UserDTO;
import com.capybarainc.BookStore.Models.User;
import com.capybarainc.BookStore.Repositories.UserRepository;
import com.capybarainc.BookStore.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public List<UserDTO> Get() {
        return userRepository.findAll().stream()
                .map(userService::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO GetOne(@PathVariable long id) {
        Optional<UserDTO> userDTO = userRepository.findById(id).map(userService::mapToUserDTO);
        return userDTO.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found")
        );
    }

    @PostMapping("")
    public UserDTO Post(@RequestBody UserDTO userDTO) {
        UserDTO savedUserDTO = userService.mapToUserDTO(userRepository.save(userService.mapToUser(userDTO)));
        return savedUserDTO;
    }

    @PutMapping("/{id}")
    public UserDTO Put(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
        if (userRepository.findById(id).isPresent()) {
            userDTO.setId(id);
            UserDTO updatedUserDTO = userService.mapToUserDTO(userRepository.save(userService.mapToUser(userDTO)));
            return updatedUserDTO;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Entity not found"
            );
        }
    }

    @PatchMapping("/{id}")
    public User Patch(@PathVariable Long id, @RequestBody JsonPatch jsonPatch)
            throws JsonPatchException, JsonProcessingException {
            Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userService.patch(id, jsonPatch);
            return user.get();
        } else {
            return User.builder().build();
        }
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable Long id, HttpServletResponse response) {
        userRepository.deleteById(id);
        response.setStatus(204);
    }

    @PostMapping(value="register")
    public String Register(@RequestBody MultiValueMap<String, String> formData) {
        String login = formData.get("login").getFirst();
        String email = formData.get("email").getFirst();;
        String password = formData.get("password").getFirst();;
        try {
            if(!userRepository.findByLogin(login).isEmpty()) throw new Exception("Login already exists");
            if(!userRepository.findByEmail(email).isEmpty()) throw new Exception("Email already exists");
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            User user = User.builder().login(login).email(email).password(new String(Base64.getEncoder().encode(hashedPassword))).salt(new String(Base64.getEncoder().encode(salt))).build();
            userRepository.save(user);
            Algorithm algorithm = userService.GetAlgorithm();
            String jwtToken = JWT.create()
                    .withIssuer(UserService.ISSUER)
                    .withSubject("User detail")
                    .withClaim("login", login)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("role", "user")
                    .sign(algorithm);
            return jwtToken;
        } catch (Exception e) {
            return e.toString();
        }
    }

    @GetMapping("login")
    public String Login(String login, String password) {
        try {
            List<User> users = userRepository.findByLogin(login);
            if (!users.isEmpty()) {
                byte[] salt = Base64.getDecoder().decode(users.get(0).getSalt().getBytes("utf-8"));
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(salt);
                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                if(
                        Arrays.equals(
                                hashedPassword,
                                Base64.getDecoder().decode(users.get(0).getPassword().getBytes(StandardCharsets.UTF_8))
                        )
                ) {
                    Algorithm algorithm = userService.GetAlgorithm();
                    String jwtToken = JWT.create()
                        .withIssuer(UserService.ISSUER)
                        .withSubject("User detail")
                        .withClaim("login", login)
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))
                        .withJWTId(UUID.randomUUID().toString())
                        .sign(algorithm);
                    return jwtToken;
                } else {
                    return "Wrong password";
                }
            } else {
                return "User with giver login doesn't exist";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }
}
