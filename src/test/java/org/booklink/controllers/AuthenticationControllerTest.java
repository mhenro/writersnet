package org.booklink.controllers;

import org.booklink.config.RootConfigTest;
import org.booklink.repositories.SectionRepository;
import org.booklink.repositories.UserRepository;
import org.booklink.services.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 16.11.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .build();
    }

    @Test
    public void activate() throws Exception {
        when(authenticationService.activate("correct")).thenReturn(true);
        mvc.perform(get("/activate").param("activationToken", "correct")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'User activation was completed! Please log-in.'}"));
        mvc.perform(get("/activate").param("activationToken", "wrong")).andExpect(status().isForbidden()).andExpect(content().json("{code: 3, message: 'Activation user error'}"));
        mvc.perform(get("/activate").param("wrong", "correct")).andExpect(status().isBadRequest());
        mvc.perform(get("/wrong").param("activationToken", "correct")).andExpect(status().isNotFound());
    }
}
