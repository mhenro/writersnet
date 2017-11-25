package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.User;
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

    @Before
    public void init() {
        final User user1 = new User();
        user1.setUsername("mhenro");
        user1.setEnabled(true);
        final User user2 = new User();
        user2.setUsername("zazaka");
        user2.setEnabled(false);
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    public void findOne() throws Exception {
        User user = authorRepository.findOne("mhenro");
        Assert.assertEquals("mhenro", user.getUsername());
    }

    @Test
    public void findAllEnable() throws Exception {
        Page<User> users = authorRepository.findAllEnabled(null);
        Assert.assertEquals(1, users.getTotalElements());
        Assert.assertEquals("mhenro", users.getContent().get(0).getUsername());
    }

    @Test
    public void save() throws Exception {
        final User user = new User();
        user.setUsername("newUser");
        authorRepository.save(user);
        Page<User> users = authorRepository.findAll((Pageable)null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("newUser", users.getContent().get(2).getUsername());
    }
}
