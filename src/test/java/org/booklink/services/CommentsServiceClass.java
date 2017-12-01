package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookComments;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.AuthorInfo;
import org.booklink.models.request_models.BookComment;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookCommentsRepository;
import org.booklink.repositories.BookRepository;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by mhenr on 23.11.2017.
 */
@RunWith(SpringRunner.class)
public class CommentsServiceClass {
    @TestConfiguration
    static class AuthenticationServiceConfiguration {
        @MockBean
        private Environment env;
        @MockBean
        private BookCommentsRepository bookCommentsRepository;
        @MockBean
        private BookRepository bookRepository;
        @MockBean
        private AuthorRepository authorRepository;
        @Bean
        public CommentsService commentsService() {
            return new CommentsService(env, bookCommentsRepository, bookRepository, authorRepository);
        }
    }

    @Autowired
    private Environment env;

    @Autowired
    private BookCommentsRepository bookCommentsRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CommentsService commentsService;

    @Before
    public void init() {
        List<BookComments> comments = generateComments(2);
        final Page<BookComments> page = new PageImpl<>(comments);
        Mockito.when(env.getProperty("writersnet.avatarwebstorage.path")).thenReturn("https://localhost/css/images/avatars/");
        Mockito.when(bookCommentsRepository.findAllByBookId(4L, null)).thenReturn(page);
        final User user = new User();
        user.setUsername("user");
        final Book book = new Book();
        book.setId(4L);
        book.setAuthor(user);
        Mockito.when(bookRepository.findOne(book.getId())).thenReturn(book);
        Mockito.when(authorRepository.findOne(user.getUsername())).thenReturn(user);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void getComments() throws Exception {
        Page<BookComments> comments = commentsService.getComments(4L, null);
        Assert.assertEquals(2, comments.getTotalElements());
        Assert.assertEquals("https://localhost/css/images/avatars/default_avatar.png", comments.getContent().get(0).getAuthorInfo().getAvatar());
        Assert.assertEquals("Anonymous", comments.getContent().get(0).getAuthorInfo().getFirstName());
        Assert.assertEquals("", comments.getContent().get(0).getAuthorInfo().getLastName());
        Assert.assertEquals("avatar_path", comments.getContent().get(1).getAuthorInfo().getAvatar());
        Assert.assertEquals("First", comments.getContent().get(1).getAuthorInfo().getFirstName());
        Assert.assertEquals("Last", comments.getContent().get(1).getAuthorInfo().getLastName());
    }

    @Test
    public void getComments_wrongBook() throws Exception {
        Page<BookComments> comments = commentsService.getComments(5L, null);
        Assert.assertEquals(null, comments);
    }

    @Test(expected = Test.None.class)
    public void saveComment() throws Exception {
        final BookComment bookComment = new BookComment();
        bookComment.setBookId(4L);
        commentsService.saveComment(bookComment);
    }

    @Test(expected = Test.None.class)
    public void saveComment_withUser() throws Exception {
        final BookComment bookComment = new BookComment();
        bookComment.setBookId(4L);
        bookComment.setUserId("user");
        commentsService.saveComment(bookComment);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void saveComment_wrongUser() throws Exception {
        final BookComment bookComment = new BookComment();
        bookComment.setBookId(4L);
        bookComment.setUserId("user2");
        commentsService.saveComment(bookComment);
    }

    @Test(expected = Test.None.class)
    public void deleteComment() throws Exception {
        commentsService.deleteComment(4L, 150L);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteComment_wrongBook() throws Exception {
        commentsService.deleteComment(44L, 150L);
    }

    @Test(expected = UnauthorizedUserException.class)
    public void deleteComment_unauthorized() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("user2");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        commentsService.deleteComment(4L, 150L);
    }

    private List<BookComments> generateComments(final int count) {
        return IntStream.range(0, count).mapToObj(this::createComment).collect(Collectors.toList());
    }

    private BookComments createComment(final int i) {
        final BookComments bookComments = new BookComments();
        final User user = new User();
        user.setAvatar("avatar_path");
        user.setFirstName("First");
        user.setLastName("Last");
        if (i == 1) {
            bookComments.setUser(user);
        }
        return bookComments;
    }
}