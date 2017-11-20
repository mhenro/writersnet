package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookText;
import org.booklink.models.entities.Section;
import org.booklink.models.entities.User;
import org.booklink.models.request_models.Credentials;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.SectionRepository;
import org.booklink.repositories.UserRepository;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

        @Bean
        public AuthorService authorService() {
            return new AuthorService(env, authorRepository);
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
        List<User> authors = generateAuthors(2);
        Mockito.when(env.getProperty("writersnet.coverwebstorage.path")).thenReturn("https://localhost/css/images/covers/");
        Mockito.when(env.getProperty("writersnet.avatarwebstorage.path")).thenReturn("https://localhost/css/images/avatars/");
        Mockito.when(authorRepository.findOne(authors.get(0).getUsername())).thenReturn(authors.get(0));
        Mockito.when(authorRepository.findOne(authors.get(1).getUsername())).thenReturn(authors.get(1));
    }

    @Test
    public void getAuthor() throws Exception {
        User user = authorService.getAuthor("user");
        Assert.assertEquals("user", user.getUsername());
        Assert.assertEquals("", user.getPassword());
        Assert.assertEquals("", user.getActivationToken());
        Assert.assertEquals("", user.getAuthority());
        Assert.assertEquals(true, user.getEnabled());
        Assert.assertEquals(null, user.getSection().getAuthor());
        Assert.assertEquals("https://localhost/css/images/avatars/default_avatar.png", user.getAvatar());

        List<Book> books = new ArrayList<>(user.getBooks());
        Assert.assertEquals(null, books.get(0).getAuthor());
        Assert.assertEquals("https://localhost/css/images/covers/default_cover.png", books.get(0).getCover());
        Assert.assertEquals(5, (int)books.get(0).getSize());
        Assert.assertEquals(null, books.get(0).getBookText());
        Assert.assertEquals(null, books.get(1).getAuthor());
        Assert.assertEquals("http://cover.png", books.get(1).getCover());
        Assert.assertEquals(5, (int)books.get(1).getSize());
        Assert.assertEquals(null, books.get(1).getBookText());
    }

    private Set<Book> generateBooks(final int count) {
        Set<Book> books = IntStream.of(0, count).mapToObj(this::createBook).collect(Collectors.toSet());
        return books;
    }

    private Book createBook(final int i) {
        final Book book = new Book();
        book.setId(Long.valueOf(i));
        book.setName("Book #" + i);
        book.setAuthor(new User());
        book.setCover(i == 0 ? "http://cover.png" : null);
        final BookText bookText = new BookText();
        bookText.setId(book.getId());
        bookText.setText("Book" + i);
        book.setBookText(bookText);
        return book;
    }

    private Section createSection(final User author) {
        final Section section = new Section();
        section.setAuthor(author);
        return section;
    }

    private List<User> generateAuthors(final int count) {
        List<User> authors = IntStream.of(0, count).mapToObj(this::createAuthor).collect(Collectors.toList());
        return authors;
    }

    private User createAuthor(final int i) {
        final User user = new User();
        user.setUsername("user");
        user.setPassword("$2a$10$9deKO8TOxquIiUstzBuJLO8lMkSaZX/yxG2Ix/OK5Tl5TMVbkxeP6");
        user.setActivationToken("token111");
        user.setAuthority("ROLE_USER");
        user.setEnabled(true);
        user.setBooks(generateBooks(2));
        user.setSection(createSection(user));
        user.setAvatar(i == 0 ? "http://avatar.png" : null);
        return user;
    }
}
