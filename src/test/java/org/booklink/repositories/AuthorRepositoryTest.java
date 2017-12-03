package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.*;
import org.booklink.models.top_models.TopAuthorBookCount;
import org.booklink.models.top_models.TopAuthorComments;
import org.booklink.models.top_models.TopAuthorRating;
import org.booklink.models.top_models.TopAuthorViews;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by mhenr on 17.11.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class AuthorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    private User createUser(final String username, final boolean enabled, final long views) {
        final User user = new User();
        user.setUsername(username);
        user.setEnabled(enabled);
        user.setViews(views);
        entityManager.persist(user);

        return user;
    }

    private Book createBook(final User user) {
        final Book book = new Book();
        book.setAuthor(user);
        book.setName("book");
        entityManager.persist(book);

        return book;
    }

    private BookComments createComment(final User user, final Book book) {
        final BookComments bookComments = new BookComments();
        bookComments.setUser(user);
        bookComments.setBook(book);
        bookComments.setComment("comment");
        entityManager.persist(bookComments);

        return bookComments;
    }

    private Rating createRating(final Book book, final int estimation, final String ip) {
        final Rating rating = new Rating();
        final RatingId ratingId = new RatingId();
        ratingId.setClientIp(ip);
        ratingId.setEstimation(estimation);
        ratingId.setBookId(book.getId());
        rating.setRatingId(ratingId);
        entityManager.persist(rating);

        return rating;
    }

    public void init() {
        createUser("mhenro", true, 0);
        createUser("zazaka", false, 0);

        entityManager.flush();
    }

    private void initForTops() {
        entityManager.clear();
        final User userPrev = createUser("prev", true, 1);
        final User userCurrent = createUser("current", true, 2);
        final User userNext = createUser("next", true, 3);
        final Book bookPrev = createBook(userPrev);
        final Book bookCurrent1 = createBook(userCurrent);
        final Book bookCurrent2 = createBook(userCurrent);
        final Book bookNext1 = createBook(userNext);
        final Book bookNext2 = createBook(userNext);
        final Book bookNext3 = createBook(userNext);
        createComment(userPrev, bookPrev);
        createComment(userCurrent, bookCurrent1);
        createComment(userCurrent, bookCurrent2);
        createComment(userNext, bookNext1);
        createComment(userNext, bookNext2);
        createComment(userNext, bookNext3);
        createRating(bookPrev, 3, "127.0.0.1");
        createRating(bookPrev, 4, "127.0.0.2");
        createRating(bookPrev, 4, "127.0.0.3");
        createRating(bookCurrent1, 4, "127.0.0.1");
        createRating(bookCurrent2, 5, "127.0.0.1");
        createRating(bookCurrent2, 5, "127.0.0.2");
        createRating(bookNext1, 2, "127.0.0.1");
        createRating(bookNext1, 2, "127.0.0.2");
        createRating(bookNext1, 2, "127.0.0.3");
        createRating(bookNext2, 4, "127.0.0.1");
        createRating(bookNext2, 4, "127.0.0.2");
        createRating(bookNext2, 4, "127.0.0.3");
        createRating(bookNext3, 4, "127.0.0.1");
        createRating(bookNext3, 4, "127.0.0.2");
        createRating(bookNext3, 5, "127.0.0.3");

        entityManager.flush();
    }

    @Test
    public void findAllEnable() throws Exception {
        init();
        Page<User> users = authorRepository.findAllEnabled(null);
        Assert.assertEquals(1, users.getTotalElements());
        Assert.assertEquals("mhenro", users.getContent().get(0).getUsername());
    }

    @Test
    public void findAllByRating() throws Exception {
        initForTops();
        Page<TopAuthorRating> users = authorRepository.findAllByRating(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(31, users.getContent().get(0).getTotalEstimation());
        Assert.assertEquals(9, users.getContent().get(0).getTotalVotes());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(14, users.getContent().get(1).getTotalEstimation());
        Assert.assertEquals(3, users.getContent().get(1).getTotalVotes());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(11, users.getContent().get(2).getTotalEstimation());
        Assert.assertEquals(3, users.getContent().get(2).getTotalVotes());
    }

    @Test
    public void findAllByBookCount() throws Exception {
        initForTops();
        Page<TopAuthorBookCount> users = authorRepository.findAllByBookCount(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(3, users.getContent().get(0).getBookCount());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(2, users.getContent().get(1).getBookCount());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(1, users.getContent().get(2).getBookCount());
    }

    @Test
    public void findAllByComments() throws Exception {
        initForTops();
        Page<TopAuthorComments> users = authorRepository.findAllByComments(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(3, users.getContent().get(0).getCommentsCount());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(2, users.getContent().get(1).getCommentsCount());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(1, users.getContent().get(2).getCommentsCount());
    }

    @Test
    public void findAllByViews() throws Exception {
        initForTops();
        Page<TopAuthorViews> users = authorRepository.findAllByViews(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(3, users.getContent().get(0).getViews());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(2, users.getContent().get(1).getViews());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(1, users.getContent().get(2).getViews());
    }

    @Test
    public void findOne() throws Exception {
        init();
        User user = authorRepository.findOne("mhenro");
        Assert.assertEquals("mhenro", user.getUsername());
    }

    @Test
    public void save() throws Exception {
        init();
        final User user = new User();
        user.setUsername("newUser");
        authorRepository.save(user);
        Page<User> users = authorRepository.findAll((Pageable)null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("newUser", users.getContent().get(2).getUsername());
    }
}
