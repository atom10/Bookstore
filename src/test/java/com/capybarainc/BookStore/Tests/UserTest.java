package com.capybarainc.BookStore.Tests;

import com.capybarainc.BookStore.Controllers.UserController;
import com.capybarainc.BookStore.Methods.Verify;
import com.capybarainc.BookStore.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private Verify verify;
    @Test
    public void TestUserCreation()  throws Exception {
        Map<String, String> formData = new HashMap<>();
        formData.put("login", "Jan");
        formData.put("password", "12345");
        formData.put("email", "test");
        String json = objectMapper.writeValueAsString(formData);

        mvc.perform(MockMvcRequestBuilders.post("/user/register").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON)).andDo(System.out::println)
                .andExpect(status().isOk());
        Assert.isTrue(userRepository.findByLogin("Jan").size() > 0, "User failed to create");
    }

    @Test
    public void TestTokenVerify()  throws Exception {
        Map<String, String> formData = new HashMap<>();
        formData.put("login", "Jan");
        formData.put("password", "12345");
        formData.put("email", "test");
        String json = objectMapper.writeValueAsString(formData);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user/register").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON)).andDo(System.out::println)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        content = content.replace("{\"token\":\"", "").replace("\"}", "");
        final String token = content;
        assertDoesNotThrow(() -> verify.VerifyToken(token));
    }
}
