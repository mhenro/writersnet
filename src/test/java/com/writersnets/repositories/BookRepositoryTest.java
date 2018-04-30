package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.*;
import com.writersnets.models.top_models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 02.12.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private User createUser(final String userName) {
        final User user = new User();
        user.setUsername(userName);
        entityManager.persist(user);

        return user;
    }

    private BookText createBookText(final String text) {
        final BookText bookText = new BookText();
        bookText.setText(text);
        entityManager.persist(bookText);

        return bookText;
    }

    private Book createBook(final String name, final User user, final LocalDateTime lastUpdate, final BookText bookText) {
        final Book book = new Book();
        book.setName(name);
        book.setAuthor(user);
        book.setLastUpdate(lastUpdate);
        book.setBookText(bookText);
        entityManager.persist(book);

        return book;
    }

    private void createRating(final String ip, final int estimation, final Book book) {
        final Rating rating = new Rating();
        final RatingId ratingId = new RatingId();
        ratingId.setClientIp(ip);
        ratingId.setEstimation(estimation);
        ratingId.setBook(book);
        rating.setRatingId(ratingId);
        entityManager.persist(rating);
    }

    private void createComment(final User user, final Book book) {
        final Comment bookComments = new Comment();
        bookComments.setUser(user);
        bookComments.setBook(book);
        bookComments.setComment("comment");
        entityManager.persist(bookComments);
    }

    @Before
    public void init() {
        final User user = createUser("mhenro");
        final BookText prevBookText = createBookText("a");
        final BookText currentBookText = createBookText("bb");
        final BookText nextBookText = createBookText("ccc");
        final LocalDateTime currentDate = LocalDateTime.now();
        final LocalDateTime prevDate = currentDate.minusDays(1);
        final LocalDateTime nextDate = currentDate.plusDays(1);
        final Book bookPrev = createBook("prev", user, prevDate, prevBookText);
        final Book bookCurrent = createBook("current", user, currentDate, currentBookText);
        final Book bookNext = createBook("next", user, nextDate, nextBookText);
        createRating("127.0.0.1", 3, bookPrev);
        createRating("127.0.0.1", 4, bookCurrent);
        createRating("127.0.0.2", 4, bookCurrent);
        createRating("127.0.0.1", 5, bookNext);
        createRating("127.0.0.2", 5, bookNext);
        createRating("127.0.0.3", 5, bookNext);
        createComment(user, bookPrev);
        createComment(user, bookCurrent);
        createComment(user, bookCurrent);
        createComment(user, bookNext);
        createComment(user, bookNext);
        createComment(user, bookNext);
        bookPrev.setViews(0L);
        bookCurrent.setViews(2L);
        bookNext.setViews(3L);

        entityManager.flush();
    }

    @Test
    public void findAllByLastUpdate() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookNovelties> books = bookRepository.findAllByLastUpdate(pageable);
        Assert.assertNotEquals(null, books);
        Assert.assertEquals(3, books.getTotalElements());
        Assert.assertEquals("next", books.getContent().get(0).getName());
        Assert.assertEquals("current", books.getContent().get(1).getName());
        Assert.assertEquals("prev", books.getContent().get(2).getName());
    }

    @Test
    public void findAllByRating() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookRating> books = bookRepository.findAllByRating(pageable);
        Assert.assertNotEquals(null, books);
        Assert.assertEquals(3, books.getTotalElements());
        Assert.assertEquals("next", books.getContent().get(0).getName());
        Assert.assertEquals(3, books.getContent().get(0).getCount());
        Assert.assertEquals(15, books.getContent().get(0).getTotalRating());
        Assert.assertEquals("current", books.getContent().get(1).getName());
        Assert.assertEquals(2, books.getContent().get(1).getCount());
        Assert.assertEquals(8, books.getContent().get(1).getTotalRating());
        Assert.assertEquals("prev", books.getContent().get(2).getName());
        Assert.assertEquals(1, books.getContent().get(2).getCount());
        Assert.assertEquals(3, books.getContent().get(2).getTotalRating());
    }

    @Test
    public void findAllByVolume() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookVolume> books = bookRepository.findAllByVolume(pageable);
        Assert.assertNotEquals(null, books);
        Assert.assertEquals(3, books.getTotalElements());
        Assert.assertEquals("next", books.getContent().get(0).getName());
        Assert.assertEquals(3, books.getContent().get(0).getVolume());
        Assert.assertEquals("current", books.getContent().get(1).getName());
        Assert.assertEquals(2, books.getContent().get(1).getVolume());
        Assert.assertEquals("prev", books.getContent().get(2).getName());
        Assert.assertEquals(1, books.getContent().get(2).getVolume());
    }

    @Test
    public void findAllByComments() {
       /* final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookComments> books = bookRepository.findAllByComments(pageable);
        Assert.assertNotEquals(null, books);
        Assert.assertEquals(3, books.getTotalElements());
        Assert.assertEquals("next", books.getContent().get(0).getName());
        Assert.assertEquals(3, books.getContent().get(0).getCount());
        Assert.assertEquals("current", books.getContent().get(1).getName());
        Assert.assertEquals(2, books.getContent().get(1).getCount());
        Assert.assertEquals("prev", books.getContent().get(2).getName());
        Assert.assertEquals(1, books.getContent().get(2).getCount());*/
    }

    @Test
    public void findAllByViews() {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<TopBookViews> books = bookRepository.findAllByViews(pageable);
        Assert.assertNotEquals(null, books);
        Assert.assertEquals(3, books.getTotalElements());
        Assert.assertEquals("next", books.getContent().get(0).getName());
        Assert.assertEquals(3, books.getContent().get(0).getViews());
        Assert.assertEquals("current", books.getContent().get(1).getName());
        Assert.assertEquals(2, books.getContent().get(1).getViews());
        Assert.assertEquals("prev", books.getContent().get(2).getName());
        Assert.assertEquals(0, books.getContent().get(2).getViews());
    }
}
