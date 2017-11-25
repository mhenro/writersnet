package org.booklink.services;

import com.sun.org.apache.xpath.internal.operations.Mult;
import liquibase.util.file.FilenameUtils;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookText;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.BookTextRequest;
import org.booklink.models.request_models.CoverRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;

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
        Mockito.when(env.getProperty("writersnet.coverstorage.path")).thenReturn("c:\\Java\\nginx\\html\\css\\images\\covers\\");
        Mockito.when(env.getProperty("writersnet.tempstorage")).thenReturn("c:\\Java\\nginx\\html\\temp\\");
        Mockito.when(bookRepository.findAll((Pageable)null)).thenReturn(page);
        Mockito.when(bookRepository.findOne(145L)).thenReturn(createBook(145));
        final Book book = new Book();
        final User user = new User();
        user.setUsername("user0");
        book.setAuthor(user);
        Mockito.when(bookRepository.findOne(111L)).thenReturn(book);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user0");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
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

    @Test
    public void getBook() throws Exception {
        final Book book = bookService.getBook(145L);
        Assert.assertEquals("", book.getAuthor().getPassword());
        Assert.assertEquals("", book.getAuthor().getAuthority());
        Assert.assertEquals("", book.getAuthor().getActivationToken());
        Assert.assertEquals(7, (int)book.getSize());
        Assert.assertEquals(111L, (long)book.getBookText().getId());
        Assert.assertEquals("text!!!", book.getBookText().getText());
        Assert.assertEquals(null, book.getAuthor().getBooks());
        Assert.assertEquals(null, book.getAuthor().getSection());
    }

    @Test(expected = Test.None.class)
    public void saveNewBook() throws Exception {
        final Book book = new Book();
        book.setAuthorName("user0");
        bookService.saveBook(book);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveNewBook_unathorized() throws Exception {
        final Book book = new Book();
        book.setAuthorName("user10");
        bookService.saveBook(book);
    }

    @Test(expected = Test.None.class)
    public void saveExistedBook() throws Exception {
        final Book book = new Book();
        book.setId(111L);
        bookService.saveBook(book);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveExistedBook_unauthorized() throws Exception {
        final Book bookEntity = new Book();
        final User user = new User();
        user.setUsername("user5");
        bookEntity.setAuthor(user);
        Mockito.when(bookRepository.findOne(111L)).thenReturn(bookEntity);

        final Book book = new Book();
        book.setId(111L);
        bookService.saveBook(book);
    }

    @Test(expected = Test.None.class)
    public void saveCover() throws Exception {
        final CoverRequest coverRequest = new CoverRequest();
        final MultipartFile cover = Mockito.mock(MultipartFile.class);
        coverRequest.setUserId("user0");
        coverRequest.setId(111L);
        coverRequest.setCover(cover);
        bookService.saveCover(coverRequest);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void saveCover_bookNotFound() throws Exception {
        final CoverRequest coverRequest = new CoverRequest();
        final MultipartFile cover = Mockito.mock(MultipartFile.class);
        coverRequest.setUserId("user0");
        coverRequest.setId(881L);
        coverRequest.setCover(cover);
        bookService.saveCover(coverRequest);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveCover_unauthorized() throws Exception {
        final CoverRequest coverRequest = new CoverRequest();
        final MultipartFile cover = Mockito.mock(MultipartFile.class);
        coverRequest.setUserId("user1");
        coverRequest.setId(111L);
        coverRequest.setCover(cover);
        bookService.saveCover(coverRequest);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveCover_unauthorized2() throws Exception {
        final Book book = new Book();
        final User user = new User();
        user.setUsername("user10");
        book.setAuthor(user);
        Mockito.when(bookRepository.findOne(111L)).thenReturn(book);

        final CoverRequest coverRequest = new CoverRequest();
        final MultipartFile cover = Mockito.mock(MultipartFile.class);
        coverRequest.setUserId("user0");
        coverRequest.setId(111L);
        coverRequest.setCover(cover);
        bookService.saveCover(coverRequest);
    }

    //TODO: fix this test
    //@Test(expected = Test.None.class)
    public void saveText() throws Exception {
        final BookTextRequest bookTextRequest = new BookTextRequest();
        final MultipartFile text = Mockito.mock(MultipartFile.class);
        bookTextRequest.setBookId(111L);
        bookTextRequest.setUserId("user0");
        bookTextRequest.setText(text);
        bookService.saveBookText(bookTextRequest);
    }

    @Test(expected = Test.None.class)
    public void deleteBook() throws Exception {
        bookService.deleteBook(111L);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteBook_notFound() throws Exception {
        bookService.deleteBook(155L);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void deleteBook_unauthorized() throws Exception {
        final Book book = new Book();
        final User user = new User();
        user.setUsername("user10");
        book.setAuthor(user);
        Mockito.when(bookRepository.findOne(111L)).thenReturn(book);

        bookService.deleteBook(111L);
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
        book.setId(Long.valueOf(i));
        return book;
    }
}
