package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.books.BookText;
import com.writersnets.models.entities.books.Comment;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.CommentResponse;
import org.junit.Assert;
import org.junit.Before;
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
        final Comment bookComments = new Comment();
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
        final Page<CommentResponse> comments = bookCommentsRepository.findAllByBookId(1L, pageable);
        Assert.assertEquals(2, comments.getTotalElements());
    }
}
