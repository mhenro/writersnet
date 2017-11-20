package org.booklink.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booklink.models.entities.BookComments;
import org.booklink.models.request_models.BookComment;
import org.booklink.services.CommentsService;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class CommentsControllerTest {
    @Mock
    private CommentsService commentsService;

    @InjectMocks
    private CommentsController commentsController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(commentsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getComments() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<BookComments> page = Mockito.mock(Page.class);
        when(commentsService.getComments(1L, pageable)).thenReturn(page);
        mvc.perform(get("/books/1/comments")).andExpect(status().isOk());
        mvc.perform(get("/books/1/wrong")).andExpect(status().isNotFound());
    }

    @Test
    public void saveComment() throws Exception {
        final BookComment bookComment = new BookComment();
        final String json = mapper.writeValueAsString(bookComment);
        mvc.perform(post("/books/comments").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Your comment was added'}"));
        mvc.perform(post("/books/comments").content(json)).andExpect(status().isUnsupportedMediaType());
        mvc.perform(post("/books/comments")).andExpect(status().isBadRequest());
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(commentsService).saveComment(any(BookComment.class));
        mvc.perform(post("/books/comments").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void deleteComment() throws Exception {
        final BookComment bookComment = new BookComment();
        final String json = mapper.writeValueAsString(bookComment);
        mvc.perform(delete("/books/1/comments/2")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Your comment was deleted'}"));
        mvc.perform(delete("/wrong/1/commets/2")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(commentsService).deleteComment(any(Long.class), any(Long.class));
        mvc.perform(delete("/books/1/comments/2").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }
}
