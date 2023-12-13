package com.capybarainc.BookStore.Tests;

import com.capybarainc.BookStore.Controllers.UserController;
import com.capybarainc.BookStore.Methods.Verify;
import com.capybarainc.BookStore.Repositories.UserRepository;
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
    private MockMvc mvc;
    @Autowired
    private Verify verify;
    @Test
    public void TestUserCreation()  throws Exception {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.set("login", "Jan");
        formData.set("password", "12345");
        formData.set("email", "test");

        mvc.perform(MockMvcRequestBuilders.post("/user/register").contentType(MediaType.APPLICATION_FORM_URLENCODED).params(formData).accept(MediaType.APPLICATION_JSON)).andDo(System.out::println)
                .andExpect(status().isOk());
        Assert.isTrue(userRepository.findByLogin("Jan").size() > 0, "User failed to create");
    }

    @Test
    public void TestTokenVerify()  throws Exception {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.set("login", "Jan");
        formData.set("password", "12345");
        formData.set("email", "test");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user/register").contentType(MediaType.APPLICATION_FORM_URLENCODED).params(formData).accept(MediaType.APPLICATION_JSON)).andDo(System.out::println)
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        assertDoesNotThrow(() -> verify.VerifyToken(content));
    }
}
