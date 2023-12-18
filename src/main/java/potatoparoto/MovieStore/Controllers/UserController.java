package potatoparoto.MovieStore.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import potatoparoto.MovieStore.Methods.Verify;
import potatoparoto.MovieStore.Models.User;
import potatoparoto.MovieStore.Repositories.UserRepository;
import potatoparoto.MovieStore.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    public final byte[] PEPPER = "pepper".getBytes("utf-8");
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    Verify verify;

    public UserController() throws UnsupportedEncodingException {
    }

    @GetMapping("/")
    public List<User> Get(@RequestHeader("Authorization") String bearerToken) {
        if(verify.VerifyTokenWithClaim(bearerToken.replace("Bearer ",""), "Role", "Admin")) return new ArrayList<>();
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User GetOne(@PathVariable long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found")
        );
    }

    @PostMapping("")
    public User Post(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @PutMapping("/{id}")
    public User Put(@PathVariable("id") Long id, @RequestBody User user) {
        if (userRepository.findById(id).isPresent()) {
            user.setId(id);
            userRepository.save(user);
            return user;
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

    @PostMapping(value="register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> Register(@RequestBody Map<String, String> formData) {
        String login = formData.get("login");
        String email = formData.get("email");
        String password = formData.get("password");
        try {
            if(!userRepository.findByLogin(login).isEmpty()) throw new Exception("Login already exists");
            if(!userRepository.findByEmail(email).isEmpty()) throw new Exception("Email already exists");
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] combined = new byte[salt.length + PEPPER.length];
            System.arraycopy(salt,0,combined,0         ,salt.length);
            System.arraycopy(PEPPER,0,combined,salt.length,PEPPER.length);
            md.update(combined);
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
            return ResponseEntity.ok("{\"token\":\""+jwtToken+"\"}");
        } catch (Exception e) {
            return ResponseEntity.ok(e.toString());
        }
    }

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> Login(@RequestBody Map<String, String> formData) {
        try {
            List<User> users = userRepository.findByLogin(formData.get("login"));
            if (!users.isEmpty()) {
                byte[] salt = Base64.getDecoder().decode(users.get(0).getSalt().getBytes("utf-8"));
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte[] combined = new byte[salt.length + PEPPER.length];
                System.arraycopy(salt,0,combined,0         ,salt.length);
                System.arraycopy(PEPPER,0,combined,salt.length,PEPPER.length);
                md.update(combined);
                byte[] hashedPassword = md.digest(formData.get("password").getBytes(StandardCharsets.UTF_8));
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
                        .withClaim("login", formData.get("login"))
                        .withIssuedAt(new Date())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 500000L))
                        .withJWTId(UUID.randomUUID().toString())
                        .sign(algorithm);
                    return ResponseEntity.ok("{\"token\":\""+jwtToken+"\"}");
                } else {
                    return ResponseEntity.ok("Wrong password");
                }
            } else {
                return ResponseEntity.ok("User with giver login doesn't exist");
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e.toString());
        }
    }
}
