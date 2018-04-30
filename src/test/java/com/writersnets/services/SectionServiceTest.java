package com.writersnets.services;

import com.writersnets.models.entities.Section;
import com.writersnets.models.entities.User;
import com.writersnets.models.response.SectionResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.SectionRepository;
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
import java.util.Optional;

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
        Mockito.when(sectionRepository.findById(5L)).thenReturn(Optional.ofNullable(section));
    }

    @Test
    public void getSection_notFound() throws Exception {
        SectionResponse section = sectionService.getSection(6L);
        Assert.assertEquals(null, section);
    }
}
