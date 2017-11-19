package org.booklink.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booklink.models.request_models.Credentials;
import org.booklink.services.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 16.11.2017.
 */
@RunWith(SpringRunner.class)
public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .build();
    }

    @Test
    public void auth() throws Exception {
        final Credentials credentials = new Credentials();
        final String json = mapper.writeValueAsString(credentials);
        when(authenticationService.auth(any(Credentials.class))).thenReturn("token111");
        mvc.perform(post("/auth").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'token111'}"));
        mvc.perform(post("/auth").content(json)).andExpect(status().isUnsupportedMediaType());
        when(authenticationService.auth(any(Credentials.class))).thenReturn(null);
        mvc.perform(post("/auth").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized()).andExpect(content().json("{code: 1, message: 'Bad credentials'}"));
        mvc.perform(post("/wrong").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        mvc.perform(get("/auth").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void register() throws Exception {
        final Credentials credentials = new Credentials();
        final String json = mapper.writeValueAsString(credentials);
        when(authenticationService.register(any(Credentials.class))).thenReturn(true);
        mvc.perform(post("/register").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'OK'}"));
        mvc.perform(post("/register").content(json)).andExpect(status().isUnsupportedMediaType());
        when(authenticationService.register(any(Credentials.class))).thenReturn(false);
        mvc.perform(post("/register").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpect(content().json("{code: 2, message: 'Object already exist'}"));
        mvc.perform(post("/wrong").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
        mvc.perform(get("/register").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void activate() throws Exception {
        when(authenticationService.activate("correct")).thenReturn(true);
        mvc.perform(get("/activate").param("activationToken", "correct")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'User activation was completed! Please log-in.'}"));
        mvc.perform(get("/activate").param("activationToken", "wrong")).andExpect(status().isForbidden()).andExpect(content().json("{code: 3, message: 'Activation user error'}"));
        mvc.perform(get("/activate").param("wrong", "correct")).andExpect(status().isBadRequest());
        mvc.perform(get("/wrong").param("activationToken", "correct")).andExpect(status().isNotFound());
        mvc.perform(post("/activate").param("activationToken", "correct")).andExpect(status().isMethodNotAllowed());
    }
}
