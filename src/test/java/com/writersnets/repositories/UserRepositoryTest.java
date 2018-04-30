package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.User;
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
 * Created by mhenr on 26.11.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void init() {
        final User user1 = new User();
        user1.setUsername("mhenro");
        user1.setActivationToken("token1");
        user1.setEmail("aaa@aaa.aa");
        final User user2 = new User();
        user2.setUsername("zazaka");
        user2.setActivationToken("token2");
        user2.setEmail("bbb@bbb.bb");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    public void findUserByActivationToken() throws Exception {
        final User user1 = userRepository.findUserByActivationToken("token1");
        final User user2 = userRepository.findUserByActivationToken("token2");
        Assert.assertEquals("mhenro", user1.getUsername());
        Assert.assertEquals("zazaka", user2.getUsername());
    }
    @Test
    public void findUserByEmail() throws Exception {
        final User user1 = userRepository.findUserByEmail("aaa@aaa.aa");
        final User user2 = userRepository.findUserByEmail("bbb@bbb.bb");
        Assert.assertEquals("mhenro", user1.getUsername());
        Assert.assertEquals("zazaka", user2.getUsername());
    }

}
