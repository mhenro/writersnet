package org.booklink.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booklink.models.entities.Book;
import org.booklink.models.top_models.*;
import org.booklink.services.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 02.12.2017.
 */
@RunWith(SpringRunner.class)
public class BookTopControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookTopController bookTopController;

    private MockMvc mvc;

    @Before
    public void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookTopController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getTopNovelties() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookNovelties> page = Mockito.mock(Page.class);
        when(bookService.getBooksByLastUpdate(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/novelties")).andExpect(status().isOk());
    }

    @Test
    public void getTopRating() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookRating> page = Mockito.mock(Page.class);
        when(bookService.getBooksByRating(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/rating")).andExpect(status().isOk());
    }

    @Test
    public void getTopVolume() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookVolume> page = Mockito.mock(Page.class);
        when(bookService.getBooksByVolume(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/volume")).andExpect(status().isOk());
    }

    @Test
    public void getTopComments() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookComments> page = Mockito.mock(Page.class);
        when(bookService.getBooksByComments(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/comments")).andExpect(status().isOk());
    }

    @Test
    public void getTopViews() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookViews> page = Mockito.mock(Page.class);
        when(bookService.getBooksByViews(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/views")).andExpect(status().isOk());
    }

    @Test
    public void wrongTop() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookNovelties> page = Mockito.mock(Page.class);
        when(bookService.getBooksByLastUpdate(pageable)).thenReturn(page);
        mvc.perform(get("/top/books/wrong")).andExpect(status().isNotFound());
    }
}
