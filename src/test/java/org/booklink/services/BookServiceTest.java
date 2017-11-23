package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookText;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.repositories.*;
import org.hibernate.cfg.CollectionSecondPass;
import org.junit.Assert;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mhenr on 23.11.2017.
 */
@RunWith(SpringRunner.class)
public class BookServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private Environment env;
        @MockBean
        private BookRepository bookRepository;
        @MockBean
        private BookCommentsRepository bookCommentsRepository;
        @MockBean
        private BookTextRepository bookTextRepository;
        @MockBean
        private SerieRepository serieRepository;
        @MockBean
        private AuthorRepository authorRepository;

        @Bean
        public BookService bookService() {
            return new BookService(env, bookRepository, bookCommentsRepository, bookTextRepository, serieRepository, authorRepository);
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
        List<Book> books = generateBooks(2);
        final Page<Book> page = new PageImpl<>(books);
        Mockito.when(env.getProperty("writersnet.coverwebstorage.path")).thenReturn("https://localhost/css/images/covers/");
        Mockito.when(bookRepository.findAll((Pageable)null)).thenReturn(page);
    }

    @Test
    public void getBooks() throws Exception {
        Page<Book> books = bookService.getBooks(null);
        Assert.assertEquals(2, books.getTotalElements());
        Assert.assertEquals("", books.getContent().get(0).getAuthor().getPassword());
        Assert.assertEquals("", books.getContent().get(0).getAuthor().getAuthority());
        Assert.assertEquals("", books.getContent().get(0).getAuthor().getActivationToken());
        Assert.assertEquals(7, (int)books.getContent().get(0).getSize());
        Assert.assertEquals(null, books.getContent().get(0).getBookText());
        Assert.assertEquals(null, books.getContent().get(0).getAuthor().getBooks());
        Assert.assertEquals(null, books.getContent().get(0).getAuthor().getSection());
    }

    private List<Book> generateBooks(final int count) {
        return IntStream.range(0, count).mapToObj(this::createBook).collect(Collectors.toList());
    }

    private Book createBook(final int i) {
        final Book book = new Book();
        final User user = new User();
        user.setPassword("password");
        user.setAuthority("role");
        user.setActivationToken("token");
        final Set<Book> books = Collections.EMPTY_SET;
        user.setBooks(books);
        final Section section = new Section();
        user.setSection(section);
        final BookText bookText = new BookText();
        bookText.setId(111L);
        bookText.setText("text!!!");
        book.setAuthor(user);
        book.setBookText(bookText);
        return book;
    }
}
