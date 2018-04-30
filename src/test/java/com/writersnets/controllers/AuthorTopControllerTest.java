package com.writersnets.controllers;

import com.writersnets.models.top_models.*;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 03.12.2017.
 */
@RunWith(SpringRunner.class)
public class AuthorTopControllerTest {
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorTopController authorTopController;

    private MockMvc mvc;

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(authorTopController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                //.setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getTopRating() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopAuthorRating> page = Mockito.mock(Page.class);
        when(authorService.getAuthorsByRating(pageable)).thenReturn(page);
        mvc.perform(get("/top/authors/rating")).andExpect(status().isOk());
    }

    @Test
    public void getTopComments() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopAuthorComments> page = Mockito.mock(Page.class);
        when(authorService.getAuthorsByComments(pageable)).thenReturn(page);
        mvc.perform(get("/top/authors/comments")).andExpect(status().isOk());
    }

    @Test
    public void getTopViews() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopAuthorViews> page = Mockito.mock(Page.class);
        when(authorService.getAuthorsByViews(pageable)).thenReturn(page);
        mvc.perform(get("/top/authors/views")).andExpect(status().isOk());
    }

    @Test
    public void wrongTop() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopAuthorRating> page = Mockito.mock(Page.class);
        when(authorService.getAuthorsByRating(pageable)).thenReturn(page);
        mvc.perform(get("/top/authors/wrong")).andExpect(status().isNotFound());
    }
}
