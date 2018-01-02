package org.booklink.controllers;

import org.booklink.models.entities.Section;
import org.booklink.models.response.SectionResponse;
import org.booklink.services.SectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class SectionControllerTest {
    @Mock
    private SectionService sectionService;

    @InjectMocks
    private SectionController sectionController;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(sectionController)
                .build();
    }

    @Test
    public void getSection() throws Exception {
        SectionResponse section = new SectionResponse();
        section.setId(1L);
        when(sectionService.getSection(section.getId())).thenReturn(section);
        mvc.perform(get("/sections/1")).andExpect(status().isOk()).andExpect(jsonPath("id", is(1)));
        mvc.perform(get("/sections/0")).andExpect(status().isNotFound());
        mvc.perform(get("/wrong")).andExpect(status().isNotFound());
    }
}
