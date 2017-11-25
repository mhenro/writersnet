package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.*;
import org.junit.After;
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

import java.util.Date;

/**
 * Created by mhenr on 25.11.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class BookCommentsRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookCommentsRepository bookCommentsRepository;

    @Before
    public void init() {
        final BookComments bookComments = new BookComments();
        final Book book = new Book();
        final User user = new User();
        final BookText bookText = new BookText();
        user.setUsername("mhenro");
        book.setBookText(bookText);
        book.setAuthor(user);
        bookComments.setUser(user);
        bookComments.setBook(book);
        entityManager.persist(user);
        entityManager.persist(bookText);
        entityManager.persist(book);
        entityManager.persist(bookComments);
        entityManager.flush();
    }

    //TODO: fixme
    //@Test
    public void findAllByBookId() throws Exception {
        final Pageable pageable = Mockito.mock(Pageable.class);
        final Page<BookComments> comments = bookCommentsRepository.findAllByBookId(1L, pageable);
        Assert.assertEquals(2, comments.getTotalElements());
    }

    @Test
    public void findAllByBookId2() throws Exception {
        final Iterable<BookComments> comments = bookCommentsRepository.findAllByBookId(1L);
        Assert.assertEquals(true, comments.iterator().hasNext());
        Assert.assertEquals(1L, (long)comments.iterator().next().getId());
    }
}
