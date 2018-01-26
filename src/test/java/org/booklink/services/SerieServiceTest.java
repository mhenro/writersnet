package org.booklink.services;

import org.booklink.models.entities.BookSerie;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.SerieRequest;
import org.booklink.models.response.BookSerieResponse;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SerieRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mhenr on 23.11.2017.
 */
@RunWith(SpringRunner.class)
public class SerieServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private SerieRepository serieRepository;
        @MockBean
        private AuthorRepository authorRepository;
        @MockBean
        private AuthorizedUserService authorizedUserService;

        @Bean
        public SerieService serieService() {
            return new SerieService(serieRepository, authorizedUserService);
        }
    }

    @Autowired
    private SerieRepository serieRepository;

    @Autowired
    private SerieService serieService;

    @Before
    public void init() {
        List<BookSerieResponse> series = generateSeries(2);
        final Page<BookSerieResponse> page = new PageImpl<>(series);
        Mockito.when(serieRepository.findAllByUserId("user", null)).thenReturn(page);
        final User author = createUser("user0");
        final BookSerie bookSerie = new BookSerie();
        bookSerie.setAuthor(author);
        Mockito.when(serieRepository.findOne(55L)).thenReturn(bookSerie);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user0");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getBookSeries() throws Exception {
        Page<BookSerieResponse> series = serieService.getBookSeries("user", null);
        Assert.assertEquals(2, series.getTotalElements());
    }

    @Test
    public void getBookSeries_userNotFound() throws Exception {
        Page<BookSerieResponse> series = serieService.getBookSeries("wrong", null);
        Assert.assertEquals(null, series);
    }

    @Test
    public void saveSerie() throws Exception {
        final SerieRequest serie = new SerieRequest();
        serie.setId(55L);
        serieService.saveSerie(serie);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveSerie_unauthorized() throws Exception {
        final SerieRequest serie = new SerieRequest();
        serie.setId(55L);
        serieService.saveSerie(serie);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void saveSerie_serieNotFound() throws Exception {
        final SerieRequest serie = new SerieRequest();
        serie.setId(54L);
        serieService.saveSerie(serie);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveSerie_unauthorized2() throws Exception {
        final User author = createUser("user1");
        final BookSerie bookSerie = new BookSerie();
        bookSerie.setAuthor(author);
        Mockito.when(serieRepository.findOne(55L)).thenReturn(bookSerie);

        final SerieRequest serie = new SerieRequest();
        serie.setId(55L);
        serieService.saveSerie(serie);
    }

    @Test(expected = Test.None.class)
    public void deleteSerie() throws Exception {
        serieService.deleteSerie(55L);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteSerie_notFound() throws Exception {
        serieService.deleteSerie(515L);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void deleteSerie_unauthorized() throws Exception {
        final User author = createUser("user10");
        final BookSerie bookSerie = new BookSerie();
        bookSerie.setAuthor(author);
        Mockito.when(serieRepository.findOne(55L)).thenReturn(bookSerie);

        serieService.deleteSerie(55L);
    }

    private List<BookSerieResponse> generateSeries(final int count) {
        return IntStream.range(0, count).mapToObj(this::createSerie).collect(Collectors.toList());
    }

    private BookSerieResponse createSerie(final int i) {
        final User author = createUser("user" + i);
        final BookSerieResponse bookSerie = new BookSerieResponse();
        bookSerie.setId(Long.valueOf(i));
        bookSerie.setName("serie" + i);
        return bookSerie;
    }

    private User createUser(final String username) {
        final User user = new User();
        user.setUsername(username);
        return user;
    }
}
