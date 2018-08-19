package com.writersnets.services;

import com.writersnets.models.entities.users.Section;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.SectionResponse;
import com.writersnets.repositories.SectionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

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
