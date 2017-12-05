package org.booklink.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.request_models.AvatarRequest;
import org.booklink.services.AuthorService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;


import static org.hamcrest.core.Is.is;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 19.11.2017.
 */
@RunWith(SpringRunner.class)
public class AuthorControllerTest {
    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(authorController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getAuthors() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<User> page = Mockito.mock(Page.class);
        when(authorService.getAuthors(pageable)).thenReturn(page);
        mvc.perform(get("/authors")).andExpect(status().isOk());
        mvc.perform(get("/wrong")).andExpect(status().isNotFound());
    }

    @Test
    public void getAuthor() throws Exception {
        User user = new User();
        user.setUsername("user");
        when(authorService.getAuthor(user.getUsername())).thenReturn(user);
        mvc.perform(get("/authors/user")).andExpect(status().isOk()).andExpect(jsonPath("username", is("user")));
        mvc.perform(get("/authors/wronguser")).andExpect(status().isNotFound()).andExpect(content().json("{code: 1, message: 'Author not found'}"));
        mvc.perform(get("/wrong/user")).andExpect(status().isNotFound());
        mvc.perform(post("/authors/user")).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void saveAuthor() throws Exception {
        final User user = new User();
        final String json = mapper.writeValueAsString(user);
        mvc.perform(post("/authors").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Author was saved'}"));
        mvc.perform(post("/authors").content(json)).andExpect(status().isUnsupportedMediaType());
        mvc.perform(post("/authors")).andExpect(status().isBadRequest());
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(authorService).saveAuthor(any(User.class));
        mvc.perform(post("/authors").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void saveAvatar() throws Exception {
        final User user = new User();
        final AvatarRequest avatarRequest = new AvatarRequest();
        final String json = mapper.writeValueAsString(avatarRequest);
        mvc.perform(post("/avatar").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Avatar was saved successfully'}"));
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        mvc.perform(get("/avatar")).andExpect(status().isMethodNotAllowed());
        doThrow(new RuntimeException("test error")).when(authorService).saveAvatar(any(AvatarRequest.class));
        mvc.perform(post("/avatar").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void subscribeOnUser() throws Exception {
        final Response<String> response = new Response<>();
        response.setMessage("111");
        when(authorService.subscribeOnUser("user2")).thenReturn(response);
        final String json = mapper.writeValueAsString("user2");
        mvc.perform(post("/authors/subscribe").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: '111'}"));
    }
}
