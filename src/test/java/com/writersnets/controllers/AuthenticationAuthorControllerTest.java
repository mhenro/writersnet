package com.writersnets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.writersnets.models.entities.User;
import com.writersnets.models.response.ChatGroupResponse;
import com.writersnets.security.JwtFilter;
import com.writersnets.services.AuthorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 07.12.2017.
 */
@RunWith(SpringRunner.class)
public class AuthenticationAuthorControllerTest {

    private JwtFilter filter = new JwtFilter();

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    private String generateActivationToken(User user) {
        String result = Jwts.builder().setSubject(user.getUsername())
                .claim("roles", user.getAuthority())
                .claim("enabled", user.getEnabled())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+15*60*1000))
                .signWith(SignatureAlgorithm.HS256, "booklink").compact();

        return result;
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(authorController)
                .addFilter(filter)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getChatGroups_authOk() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<ChatGroupResponse> page = Mockito.mock(Page.class);
        final User user = new User();
        user.setAuthority("ROLE_USER");
        user.setEnabled(true);
        final String token = generateActivationToken(user);
        when(authorService.getChatGroups("user0", pageable)).thenReturn(page);
        mvc.perform(get("/authors/user0/groups").header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test(expected = com.writersnets.models.exceptions.UnauthorizedUserException.class)
    public void getChatGroups_authFail1() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<ChatGroupResponse> page = Mockito.mock(Page.class);
        final User user = new User();
        user.setEnabled(true);
        final String token = generateActivationToken(user);
        when(authorService.getChatGroups("user0", pageable)).thenReturn(page);
        mvc.perform(get("/authors/user0/groups").header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test(expected = com.writersnets.models.exceptions.UnauthorizedUserException.class)
    public void getChatGroups_authFail2() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<ChatGroupResponse> page = Mockito.mock(Page.class);
        final User user = new User();
        user.setAuthority("ROLE_USER");
        user.setEnabled(false);
        final String token = generateActivationToken(user);
        when(authorService.getChatGroups("user0", pageable)).thenReturn(page);
        mvc.perform(get("/authors/user0/groups").header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test(expected = io.jsonwebtoken.ExpiredJwtException.class)
    public void getChatGroups_authFail3() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<ChatGroupResponse> page = Mockito.mock(Page.class);
        final User user = new User();
        user.setAuthority("ROLE_USER");
        user.setEnabled(true);
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaGVucm8iLCJyb2xlcyI6IlJPTEVfVVNFUiIsImVuYWJsZWQiOnRydWUsImlhdCI6MTUxMjY2NjIyNiwiZXhwIjoxNTEyNjY3MTI2fQ.FixxhTmHPHaPEG1SWPc7f6gJJtj63QqCZDxqX9V43fU";
        when(authorService.getChatGroups("user0", pageable)).thenReturn(page);
        mvc.perform(get("/authors/user0/groups").header("authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
