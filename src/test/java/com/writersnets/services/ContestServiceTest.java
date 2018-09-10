package com.writersnets.services;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.BookResponse;
import com.writersnets.repositories.*;
import com.writersnets.services.authors.NewsService;
import com.writersnets.services.books.BookService;
import com.writersnets.services.security.AuthorizedUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

//TODO: added tests for contest service
@RunWith(SpringRunner.class)
public class ContestServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private Environment env;
        @MockBean
        private BookRepository bookRepository;
        @MockBean
        private BookTextRepository bookTextRepository;
        @MockBean
        private SerieRepository serieRepository;
        @MockBean
        private UserBookRepository userBookRepository;
        @MockBean
        private AuthorRepository authorRepository;
        @MockBean
        private AuthorizedUserService authorizedUserService;
        @MockBean
        private NewsService newsService;

        @Bean
        public BookService bookService() {
            return new BookService(env, bookRepository, bookTextRepository, serieRepository, userBookRepository, authorRepository, authorizedUserService, newsService);
        }
    }

    @Autowired
    private Environment env;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCommentsRepository bookCommentsRepository;
    @Autowired
    private BookTextRepository bookTextRepository;
    @Autowired
    private SerieRepository serieRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookService bookService;

    @Before
    public void init() {
//        List<BookResponse> books = generateBooks(2);
//        final Page<BookResponse> page = new PageImpl<>(books);
//        Mockito.when(env.getProperty("writersnet.coverwebstorage.path")).thenReturn("https://localhost/css/images/covers/");
//        Mockito.when(env.getProperty("writersnet.coverstorage.path")).thenReturn("c:\\Java\\nginx\\html\\css\\images\\covers\\");
//        Mockito.when(env.getProperty("writersnet.tempstorage")).thenReturn("c:\\Java\\nginx\\html\\temp\\");
        //Mockito.when(bookRepository.findAll((Pageable)null)).thenReturn(page);
        // Mockito.when(bookRepository.getAllBooksSortedByDate((Pageable)null)).thenReturn(page);
        //Mockito.when(bookRepository.findAll((Pageable)null)).thenReturn(page);
        //Mockito.when(bookRepository.findOne(145L)).thenReturn(createBook(145));
        final Book book = new Book();
        final User user = new User();
        user.setUsername("user0");
        book.setAuthor(user);
        Mockito.when(bookRepository.findById(111L)).thenReturn(Optional.ofNullable(book));
        //final Friendship friendship = new Friendship();
        //final FriendshipPK friendshipPK = new FriendshipPK();
        //friendship.setFriendshipPK(friendshipPK);
        //friendship

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user0");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getBooks() throws Exception {
        //Page<BookResponse> books = bookService.getBooks(null);
        //Assert.assertEquals(2, books.getTotalElements());
        /*Assert.assertEquals("", books.getContent().get(0).getAuthor().getPassword());
        Assert.assertEquals("", books.getContent().get(0).getAuthor().getAuthority());
        Assert.assertEquals("", books.getContent().get(0).getAuthor().getActivationToken());*/
        //Assert.assertEquals(7, (int)books.getContent().get(0).getSize());
        /*Assert.assertEquals(null, books.getContent().get(0).getBookText());
        Assert.assertEquals(null, books.getContent().get(0).getAuthor().getBooks());
        Assert.assertEquals(null, books.getContent().get(0).getAuthor().getSection());*/
        //Assert.assertEquals(null, books.getContent().get(0).getAuthor().getSubscribers());
        //Assert.assertEquals(null, books.getContent().get(0).getAuthor().getSubscriptions());
    }
}
