package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.Book;
import org.booklink.models.entities.User;
import org.booklink.models.top_models.TopAuthorBookCount;
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

    private User createUser(final String username, final boolean enabled) {
        final User user = new User();
        user.setUsername(username);
        user.setEnabled(enabled);
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

    public void init() {
        createUser("mhenro", true);
        createUser("zazaka", false);

        entityManager.flush();
    }

    private void initForTops() {
        entityManager.clear();
        final User userPrev = createUser("prev", true);
        final User userCurrent = createUser("current", true);
        final User userNext = createUser("next", true);
        createBook(userPrev);
        createBook(userCurrent);
        createBook(userCurrent);
        createBook(userNext);
        createBook(userNext);
        createBook(userNext);

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
