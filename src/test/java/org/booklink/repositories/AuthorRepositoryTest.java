package org.booklink.repositories;

import org.booklink.config.RootConfigTest;
import org.booklink.models.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mhenr on 17.11.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void test() throws Exception {
        User test = new User();
        test.setUsername("mhenro");
        entityManager.persist(test);
        entityManager.flush();


        User user = authorRepository.findOne("mhenro");
        Assert.assertEquals("mhenro", user.getUsername());
    }
}
