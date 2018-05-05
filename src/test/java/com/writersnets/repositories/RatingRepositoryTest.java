package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.*;
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
        final Book book = new Book();
        final User user = new User();
        final BookText bookText = new BookText();
        user.setUsername("mhenro");
        book.setBookText(bookText);
        book.setAuthor(user);
        entityManager.persist(user);
        entityManager.persist(bookText);
        entityManager.persist(book);

        final Rating rating = new Rating();
        final RatingId ratingId = new RatingId();
        ratingId.setClientIp("127.0.0.1");
        ratingId.setEstimation(5);
        ratingId.setBook(book);
        rating.setRatingId(ratingId);

        entityManager.persist(rating);
        entityManager.flush();
    }

    @Test
    public void findRatingByBookIdAndClientIp() {
        final Rating rating = ratingRepository.findRatingByBookIdAndClientIp(1L, "127.0.0.1");
        Assert.assertNotEquals(null, rating);
        Assert.assertEquals(1L, (long)rating.getRatingId().getBook().getId());
        Assert.assertEquals("127.0.0.1", rating.getRatingId().getClientIp());
    }
}
