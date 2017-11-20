package org.booklink.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;
import org.booklink.models.request_models.BookComment;
import org.booklink.models.request_models.BookTextRequest;
import org.booklink.models.request_models.CoverRequest;
import org.booklink.services.AuthorService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class BookControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getBooks() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<Book> page = Mockito.mock(Page.class);
        when(bookService.getBooks(pageable)).thenReturn(page);
        mvc.perform(get("/books")).andExpect(status().isOk());
        mvc.perform(get("/wrong")).andExpect(status().isNotFound());
    }

    @Test
    public void getBook() throws Exception {
        final Book book = new Book();
        book.setId(24L);
        book.setName("test");
        when(bookService.getBook(book.getId())).thenReturn(book);
        mvc.perform(get("/books/24")).andExpect(status().isOk()).andExpect(jsonPath("name", is("test")));
        mvc.perform(get("/books/15")).andExpect(status().isNotFound()).andExpect(content().json("{code: 1, message: 'Book not found'}"));
        mvc.perform(get("/wrong/24")).andExpect(status().isNotFound());
        mvc.perform(post("/books/24")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void saveBook() throws Exception {
        final Book book = new Book();
        final String json = mapper.writeValueAsString(book);
        when(bookService.saveBook(any(Book.class))).thenReturn(24L);
        mvc.perform(post("/books").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: '24'}"));
        mvc.perform(post("/books").content(json)).andExpect(status().isUnsupportedMediaType());
        mvc.perform(post("/books")).andExpect(status().isBadRequest());
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(bookService).saveBook(any(Book.class));
        mvc.perform(post("/books").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void saveCover() throws Exception {
        final CoverRequest coverRequest = new CoverRequest();
        final String json = mapper.writeValueAsString(coverRequest);
        mvc.perform(post("/cover").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Cover was saved successfully'}"));
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(bookService).saveCover(any(CoverRequest.class));
        mvc.perform(post("/cover").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void saveBookText() throws Exception {
        final BookTextRequest bookTextRequest = new BookTextRequest();
        final String json = mapper.writeValueAsString(bookTextRequest);
        mvc.perform(post("/text").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Book text was saved successfully'}"));
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(bookService).saveBookText(any(BookTextRequest.class));
        mvc.perform(post("/text").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void deleteBook() throws Exception {
        mvc.perform(delete("/books/1")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Book was deleted successfully'}"));
        doThrow(new RuntimeException("test error")).when(bookService).deleteBook(any(Long.class));
        mvc.perform(delete("/books/1")).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void getGenres() throws Exception {
        mvc.perform(get("/genres")).andExpect(status().isOk()).andExpect(content().json("['FANTASY', 'SCI_FI']"));
    }
}
