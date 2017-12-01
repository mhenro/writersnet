package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mhenr on 25.11.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class RatingRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RatingRepository ratingRepository;

    @Before
    public void init() {
        final Rating rating = new Rating();
        final RatingId ratingId = new RatingId();
        final Book book = new Book();
        final User user = new User();
        final BookText bookText = new BookText();
        user.setUsername("mhenro");
        book.setBookText(bookText);
        book.setAuthor(user);
        ratingId.setClientIp("127.0.0.1");
        ratingId.setEstimation(5);
        ratingId.setBookId(1L);
        rating.setRatingId(ratingId);
        entityManager.persist(user);
        entityManager.persist(bookText);
        entityManager.persist(book);
        entityManager.persist(rating);
        entityManager.flush();
    }

    @Test
    public void findRatingByBookIdAndClientIp() {
        final Rating rating = ratingRepository.findRatingByBookIdAndClientIp(1L, "127.0.0.1");
        Assert.assertNotEquals(null, rating);
        Assert.assertEquals(1L, (long)rating.getRatingId().getBookId());
        Assert.assertEquals("127.0.0.1", rating.getRatingId().getClientIp());
    }
}