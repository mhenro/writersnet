package com.writersnets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.writersnets.controllers.books.BookController;
import com.writersnets.models.request.BookTextRequest;
import com.writersnets.models.response.BookResponse;
import com.writersnets.services.books.BookService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: add tests for contest controller
@RunWith(SpringRunner.class)
public class ContestControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        try {
            when(bookService.saveBookText(any(BookTextRequest.class))).thenReturn(LocalDateTime.parse("2017-11-12"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getBooks() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<BookResponse> page = Mockito.mock(Page.class);
        //when(bookService.getBooks(pageable)).thenReturn(page);
        mvc.perform(get("/books")).andExpect(status().isOk());
        mvc.perform(get("/wrong")).andExpect(status().isNotFound());
    }
}
