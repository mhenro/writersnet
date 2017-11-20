package org.booklink.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.booklink.services.RatingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class RatingControllerTest {
    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(ratingController)
                .build();
    }

    @Test
    public void addStar() throws Exception {
        mvc.perform(get("/books/1/rating/5")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Your vote was added'}"));
        doThrow(new RuntimeException("test error")).when(ratingService).addStar(any(Long.class), any(Integer.class), any(HttpServletRequest.class));
        mvc.perform(get("/books/1/rating/5")).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }
}
