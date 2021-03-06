package com.writersnets.services;

import com.writersnets.models.entities.users.User;
import com.writersnets.models.request.Credentials;
import com.writersnets.repositories.UserRepository;
import com.writersnets.services.security.AuthenticationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * Created by mhenr on 17.11.2017.
 */
@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private UserRepository userRepository;

        @MockBean
        private JavaMailSender mailSender;

        @MockBean
        private Environment environment;

        @Bean
        public AuthenticationService authenticationService() {
            return new AuthenticationService(userRepository, mailSender, environment);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Before
    public void init() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("$2a$10$9deKO8TOxquIiUstzBuJLO8lMkSaZX/yxG2Ix/OK5Tl5TMVbkxeP6");
        user.setActivationToken("token111");
        user.setEnabled(false);
        Mockito.when(userRepository.findById(user.getUsername())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findUserByActivationToken("token111")).thenReturn(user);
        Mockito.when(userRepository.findUserByEmail("user@mail.ru")).thenReturn(user);
    }

    @Test
    public void activate_ok() throws Exception {
        //final boolean result = authenticationService.activate("token111");
        //Assert.assertEquals(true, result);
    }

    @Test
    public void activate_wrong() throws Exception {
        //final boolean result = authenticationService.activate("token112");
        //Assert.assertEquals(false, result);
    }

    @Test
    public void register_ok() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("newUser");
        credentials.setPassword("secret");
        credentials.setEmail("newUser@mail.ru");
        //final boolean result = authenticationService.register(credentials);
        //Assert.assertEquals(true, result);
    }

    @Test
    public void register_already_existed1() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("newUser");
        credentials.setPassword("secret");
        credentials.setEmail("user@mail.ru");
        //final boolean result = authenticationService.register(credentials);
        //Assert.assertEquals(false, result);
    }

    @Test
    public void register_already_existed2() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("user");
        credentials.setPassword("secret");
        credentials.setEmail("user@mail.ru");
        //final boolean result = authenticationService.register(credentials);
        //Assert.assertEquals(false, result);
    }
}
