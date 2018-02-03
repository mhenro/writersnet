package org.booklink.services;

import org.booklink.models.entities.User;
import org.booklink.models.request.Credentials;
import org.booklink.repositories.UserRepository;
import org.junit.Assert;
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
        Mockito.when(userRepository.findOne(user.getUsername())).thenReturn(user);
        Mockito.when(userRepository.findUserByActivationToken("token111")).thenReturn(user);
        Mockito.when(userRepository.findUserByEmail("user@mail.ru")).thenReturn(user);
    }

    @Test
    public void auth_ok() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("user");
        credentials.setPassword("secret");
        credentials.setEmail("user@mail.ru");
        String result = authenticationService.auth(credentials);
        Assert.assertEquals(151, result.length());
    }

    @Test
    public void auth_userNotFound() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("mhenro");
        credentials.setPassword("secret");
        credentials.setEmail("user@mail.ru");
        String result = authenticationService.auth(credentials);
        Assert.assertEquals(null, result);
    }

    @Test
    public void auth_wrongPassword() throws Exception {
        final Credentials credentials = new Credentials();
        credentials.setUsername("user");
        credentials.setPassword("111");
        credentials.setEmail("user@mail.ru");
        String result = authenticationService.auth(credentials);
        Assert.assertEquals(null, result);
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
