package com.writersnets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.writersnets.models.entities.BookSerie;
import com.writersnets.models.request.SerieRequest;
import com.writersnets.models.response.BookSerieResponse;
import com.writersnets.services.SerieService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class SerieControllerTest {
    @Mock
    private SerieService serieService;

    @InjectMocks
    private SerieController serieController;

    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(serieController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getBookSeries() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<BookSerieResponse> page = Mockito.mock(Page.class);
        when(serieService.getBookSeries("user", pageable)).thenReturn(page);
        mvc.perform(get("/series/user")).andExpect(status().isOk());
    }

    @Test
    public void saveSerie() throws Exception {
        final SerieRequest serie = new SerieRequest();
        serie.setId(5L);
        final String json = mapper.writeValueAsString(serie);
        when(serieService.saveSerie(any(SerieRequest.class))).thenReturn(serie.getId());
        mvc.perform(post("/series").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: '" + serie.getId() + "'}"));
        mvc.perform(post("/series").content(json)).andExpect(status().isUnsupportedMediaType());
        mvc.perform(post("/series")).andExpect(status().isBadRequest());
        mvc.perform(post("/wrong")).andExpect(status().isNotFound());
        doThrow(new RuntimeException("test error")).when(serieService).saveSerie(any(SerieRequest.class));
        mvc.perform(post("/series").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }

    @Test
    public void deleteSerie() throws Exception {
        mvc.perform(delete("/series/1")).andExpect(status().isOk()).andExpect(content().json("{code: 0, message: 'Serie was deleted successfully'}"));
        doThrow(new RuntimeException("test error")).when(serieService).deleteSerie(any(Long.class));
        mvc.perform(delete("/series/1")).andExpect(status().isInternalServerError()).andExpect(content().json("{code: 1, message: 'test error'}"));
    }
}
