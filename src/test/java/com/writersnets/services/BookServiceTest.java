package com.writersnets.services;

import com.writersnets.models.entities.*;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.BookRequest;
import com.writersnets.models.request.BookTextRequest;
import com.writersnets.models.request.CoverRequest;
import com.writersnets.models.response.BookResponse;
import com.writersnets.models.response.BookWithTextResponse;
import com.writersnets.repositories.*;
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
import java.util.Optional;
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
        List<BookResponse> books = generateBooks(2);
        final Page<BookResponse> page = new PageImpl<>(books);
        Mockito.when(env.getProperty("writersnet.coverwebstorage.path")).thenReturn("https://localhost/css/images/covers/");
        Mockito.when(env.getProperty("writersnet.coverstorage.path")).thenReturn("c:\\Java\\nginx\\html\\css\\images\\covers\\");
        Mockito.when(env.getProperty("writersnet.tempstorage")).thenReturn("c:\\Java\\nginx\\html\\temp\\");
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

    @Test
    public void getBooksByLastUpdate() throws Exception{
        List<BookResponse> booksArr = generateBooks(3);
        final Page<BookResponse> page = new PageImpl<>(booksArr);
        //Mockito.when(bookRepository.getAllBooksSortedByDate((Pageable)null)).thenReturn(page);
        //Page<BookResponse> books = bookService.getBooks(null);
        //Assert.assertEquals(3, books.getTotalElements());
    }

    @Test
    public void getBook() throws Exception {
        //final BookWithTextResponse book = bookService.getBook(145L);
        //Assert.assertEquals("", book.getAuthor().getPassword());
        //Assert.assertEquals("", book.getAuthor().getAuthority());
        //Assert.assertEquals("", book.getAuthor().getActivationToken());
        //Assert.assertEquals(7, (int)book.getSize());
        //Assert.assertEquals(111L, (long)book.getBookText().getId());
        //Assert.assertEquals("text!!!", book.getBookText().getText());
        //Assert.assertEquals(null, book.getAuthor().getBooks());
        //Assert.assertEquals(null, book.getAuthor().getSection());
    }

    @Test(expected = Test.None.class)
    public void saveNewBook() throws Exception {
        final BookRequest book = new BookRequest();
        book.setAuthorName("user0");
        bookService.saveBook(book);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveNewBook_unathorized() throws Exception {
        final BookRequest book = new BookRequest();
        book.setAuthorName("user10");
        bookService.saveBook(book);
    }

    @Test(expected = Test.None.class)
    public void saveExistedBook() throws Exception {
        final BookRequest book = new BookRequest();
        book.setId(111L);
        bookService.saveBook(book);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveExistedBook_unauthorized() throws Exception {
        final Book bookEntity = new Book();
        final User user = new User();
        user.setUsername("user5");
        bookEntity.setAuthor(user);
        Mockito.when(bookRepository.findById(111L)).thenReturn(Optional.ofNullable(bookEntity));

        final BookRequest book = new BookRequest();
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
        Mockito.when(bookRepository.findById(111L)).thenReturn(Optional.ofNullable(book));

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
        Mockito.when(bookRepository.findById(111L)).thenReturn(Optional.ofNullable(book));

        bookService.deleteBook(111L);
    }

    private List<BookResponse> generateBooks(final int count) {
        return IntStream.range(0, count).mapToObj(this::createBook).collect(Collectors.toList());
    }

    private BookResponse createBook(final int i) {
        final BookResponse book = new BookResponse();
        final User user = new User();
        user.setUsername("mhenro");
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
        //book.setAuthor(user);
        //book.setBookText(bookText);
        book.setId(Long.valueOf(i));
        return book;
    }
}
