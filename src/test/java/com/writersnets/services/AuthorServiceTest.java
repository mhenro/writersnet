package com.writersnets.services;

import com.writersnets.models.Response;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.AuthorRequest;
import com.writersnets.models.request.AvatarRequest;
import com.writersnets.models.response.AuthorResponse;
import com.writersnets.models.response.AuthorShortInfoResponse;
import com.writersnets.models.response.BookResponse;
import com.writersnets.models.response.SectionResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.FriendshipRepository;
import com.writersnets.services.authors.AuthorService;
import com.writersnets.services.authors.NewsService;
import com.writersnets.services.security.AuthorizedUserService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mhenr on 20.11.2017.
 */
@RunWith(SpringRunner.class)
public class AuthorServiceTest {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private Environment env;

        @MockBean
        private AuthorRepository authorRepository;

        @MockBean
        private FriendshipRepository friendshipRepository;

        @MockBean
        private NewsService newsService;

        @MockBean
        private AuthorizedUserService authorizedUserService;

        @Bean
        public AuthorService authorService() {
            return new AuthorService(env, authorRepository, friendshipRepository, newsService, authorizedUserService);
        }
    }

    @Autowired
    private Environment env;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Before
    public void init() {
        List<AuthorShortInfoResponse> authors = generateAuthors(2);

        final Page<AuthorShortInfoResponse> page = new PageImpl<>(authors);
        Mockito.when(env.getProperty("writersnet.coverwebstorage.path")).thenReturn("https://localhost/css/images/covers/");
        Mockito.when(env.getProperty("writersnet.avatarwebstorage.path")).thenReturn("https://localhost/css/images/avatars/");
        Mockito.when(env.getProperty("writersnet.avatarstorage.path")).thenReturn("c:\\Java\\nginx\\html\\css\\images\\avatars\\");
        //Mockito.when(authorRepository.findOne(authors.get(0).getUsername())).thenReturn(authors.get(0));
        //Mockito.when(authorRepository.findOne(authors.get(1).getUsername())).thenReturn(authors.get(1));
        Mockito.when(authorRepository.findAllEnabled(null)).thenReturn(page);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user0");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getAuthors() throws Exception {
        Page<AuthorShortInfoResponse> authors = authorService.getAuthors(null);
        Assert.assertEquals(2, authors.getTotalElements());
        Assert.assertEquals("user0", authors.getContent().get(0).getUsername());
        //Assert.assertEquals("", authors.getContent().get(0).getPassword());
        //Assert.assertEquals("", authors.getContent().get(0).getActivationToken());
        //Assert.assertEquals("", authors.getContent().get(0).getAuthority());
        //Assert.assertEquals(true, authors.getContent().get(0).getEnabled());
        //Assert.assertEquals(null, authors.getContent().get(0).getSection().getAuthor());
        Assert.assertEquals("https://localhost/css/images/avatars/default_avatar.png", authors.getContent().get(0).getAvatar());

        //List<BookResponse> books = new ArrayList<>(authors.getContent().get(0).getBooks());
        /*books.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        Assert.assertEquals(null, books.get(0).getAuthor());
        Assert.assertEquals(null, books.get(0).getCover());
        Assert.assertEquals(5, (int)books.get(0).getSize());
        Assert.assertEquals(null, books.get(0).getBookText());
        Assert.assertEquals(null, books.get(1).getAuthor());
        Assert.assertEquals("http://cover.png", books.get(1).getCover());
        Assert.assertEquals(5, (int)books.get(1).getSize());
        Assert.assertEquals(null, books.get(1).getBookText());*/

        Assert.assertEquals("user1", authors.getContent().get(1).getUsername());
        //Assert.assertEquals("", authors.getContent().get(1).getPassword());
        //Assert.assertEquals("", authors.getContent().get(1).getActivationToken());
        //Assert.assertEquals("", authors.getContent().get(1).getAuthority());
        //Assert.assertEquals(true, authors.getContent().get(1).getEnabled());
        //Assert.assertEquals(null, authors.getContent().get(1).getSection().getAuthor());
        Assert.assertEquals("http://avatar.png", authors.getContent().get(1).getAvatar());

        //books = new ArrayList<>(authors.getContent().get(1).getBooks());
        /*books.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        Assert.assertEquals(null, books.get(0).getAuthor());
        Assert.assertEquals(null, books.get(0).getCover());
        Assert.assertEquals(5, (int)books.get(0).getSize());
        Assert.assertEquals(null, books.get(0).getBookText());
        Assert.assertEquals(null, books.get(1).getAuthor());
        Assert.assertEquals("http://cover.png", books.get(1).getCover());
        Assert.assertEquals(5, (int)books.get(1).getSize());
        Assert.assertEquals(null, books.get(1).getBookText());*/

        AuthorResponse notFound = authorService.getAuthor("unknown");
        Assert.assertEquals(null, notFound);

    }

    @Test
    public void getAuthor() throws Exception {
        AuthorResponse user = authorService.getAuthor("user0");
        Assert.assertEquals("user0", user.getUsername());
        /*Assert.assertEquals("", user.getPassword());
        Assert.assertEquals("", user.getActivationToken());
        Assert.assertEquals("", user.getAuthority());
        Assert.assertEquals(true, user.getEnabled());
        Assert.assertEquals(null, user.getSection().getAuthor());*/
        Assert.assertEquals("https://localhost/css/images/avatars/default_avatar.png", user.getAvatar());

        //List<BookResponse> books = new ArrayList<>(user.getBooks());
        //books.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        //Assert.assertEquals(null, books.get(0).getAuthor());
        //Assert.assertEquals("https://localhost/css/images/covers/default_cover.png", books.get(0).getCover());
        //Assert.assertEquals(5, (int)books.get(0).getSize());
        //Assert.assertEquals(null, books.get(0).getBookText());
        //Assert.assertEquals(null, books.get(1).getAuthor());
        //Assert.assertEquals("http://cover.png", books.get(1).getCover());
        //Assert.assertEquals(5, (int)books.get(1).getSize());
        //Assert.assertEquals(null, books.get(1).getBookText());

        AuthorResponse notFound = authorService.getAuthor("unknown");
        Assert.assertEquals(null, notFound);
    }

    @Test(expected = Test.None.class)
    public void saveAuthor_noExceptions() throws Exception {
        final AuthorRequest author = new AuthorRequest();
        author.setUsername("user0");
        authorService.updateAuthor(author);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveAuthor_unauthorized() throws Exception {
        final AuthorRequest author = new AuthorRequest();
        author.setUsername("wrong");
        authorService.updateAuthor(author);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void saveAuthor_notFound() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user5");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        final AuthorRequest author = new AuthorRequest();
        author.setUsername("user5");
        authorService.updateAuthor(author);
    }

    @Test(expected = Test.None.class)
    public void saveAvatar_noExceptions() throws Exception {
        final AvatarRequest avatarRequest = new AvatarRequest();
        final MultipartFile avatar = Mockito.mock(MultipartFile.class);
        avatarRequest.setUserId("user0");
        avatarRequest.setAvatar(avatar);
        authorService.saveAvatar(avatarRequest);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void saveAvatar_unauthorized() throws Exception {
        final AvatarRequest avatarRequest = new AvatarRequest();
        final MultipartFile avatar = Mockito.mock(MultipartFile.class);
        avatarRequest.setUserId("wrong");
        avatarRequest.setAvatar(avatar);
        authorService.saveAvatar(avatarRequest);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void saveAvatar_notFound() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user5");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        final AvatarRequest avatarRequest = new AvatarRequest();
        final MultipartFile avatar = Mockito.mock(MultipartFile.class);
        avatarRequest.setUserId("user5");
        avatarRequest.setAvatar(avatar);
        authorService.saveAvatar(avatarRequest);
    }

    @Test
    public void subscribeOnUser() throws Exception {
        Response<String> response = authorService.subscribeOnUser("user1");
        Assert.assertNotNull(response);
        Assert.assertEquals(0, response.getCode());
        Assert.assertEquals("You has added first1 last1 to your subscriptions", response.getMessage());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void subscribeOnUser_subscriptionNotFound() throws Exception {
        Response<String> response = authorService.subscribeOnUser("user150");
        Assert.assertNotNull(response);
    }

    @Test
    public void removeSubscription() throws Exception {
        Response<String> response = authorService.removeSubscription("user1");
        Assert.assertNotNull(response);
        Assert.assertEquals(0, response.getCode());
        Assert.assertEquals("first1 last1 was removed from your subscriptions", response.getMessage());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void removeSubscription_subscriptionNotFound() throws Exception {
        Response<String> response = authorService.removeSubscription("user150");
        Assert.assertNotNull(response);
    }

    private Set<BookResponse> generateBooks(final int count) {
        Set<BookResponse> books = IntStream.range(0, count).mapToObj(this::createBook).collect(Collectors.toSet());
        return books;
    }

    private BookResponse createBook(final int i) {
        final BookResponse book = new BookResponse();
        /*book.setId(Long.valueOf(i));
        book.setName("Book #" + i);
        book.setAuthor(new User());
        book.setCover(i == 1 ? "http://cover.png" : null);
        final BookText bookText = new BookText();
        bookText.setId(book.getId());
        bookText.setText("Book" + i);
        book.setBookText(bookText);*/
        return book;
    }

    private SectionResponse createSection(final AuthorResponse author) {
        final SectionResponse section = new SectionResponse();
        //section.setAuthor(author);
        return section;
    }

    private List<AuthorShortInfoResponse> generateAuthors(final int count) {
        List<AuthorShortInfoResponse> authors = IntStream.range(0, count).mapToObj(this::createAuthor).collect(Collectors.toList());
        return authors;
    }

    private AuthorShortInfoResponse createAuthor(final int i) {
        final AuthorShortInfoResponse user = new AuthorShortInfoResponse();
        user.setUsername("user" + i);
        //user.setPassword("$2a$10$9deKO8TOxquIiUstzBuJLO8lMkSaZX/yxG2Ix/OK5Tl5TMVbkxeP6");
        //user.setActivationToken("token111");
        //user.setAuthority("ROLE_USER");
        //user.setEnabled(true);
        user.setFirstName("first" + i);
        user.setLastName("last" + i);
        //user.setBooks(generateBooks(2));
        //user.setSection(createSection(user));
        user.setAvatar(i == 1 ? "http://avatar.png" : null);

        return user;
    }
}
