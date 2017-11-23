package org.booklink.services;

import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SectionRepository;
import org.codehaus.plexus.util.xml.SerializerXMLWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.TestTimedOutException;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by mhenr on 23.11.2017.
 */
@RunWith(SpringRunner.class)
public class SectionServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private SectionRepository sectionRepository;

        @Bean
        public SectionService sectionService() {
            return new SectionService(sectionRepository);
        }
    }

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SectionService sectionService;

    @Before
    public void init() {
        final Section section = new Section();
        final User user = new User();
        user.setPassword("password");
        user.setActivationToken("activation_token");
        user.setAuthority("role");
        section.setAuthor(user);
        Mockito.when(sectionRepository.findOne(5L)).thenReturn(section);
    }

    @Test
    public void getSection() throws Exception {
        Section section = sectionService.getSection(5L);
        Assert.assertEquals("", section.getAuthor().getPassword());
        Assert.assertEquals("", section.getAuthor().getActivationToken());
        Assert.assertEquals("", section.getAuthor().getAuthority());
    }

    @Test
    public void getSection_notFound() throws Exception {
        Section section = sectionService.getSection(6L);
        Assert.assertEquals(null, section);
    }
}
