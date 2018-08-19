package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.groups.ChatGroup;
import com.writersnets.models.entities.groups.Message;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.MessageResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 07.12.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RootConfigTest.class})
@ActiveProfiles("test")
@DataJpaTest
@DirtiesContext
public class MessageRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private User createUser(final String username, final boolean enabled, final long views) {
        final User user = new User();
        user.setUsername(username);
        user.setEnabled(enabled);
        user.setViews(views);
        user.setFirstName("first");
        user.setLastName("last");
        entityManager.persist(user);

        return user;
    }

    private Message createMessage(User user, final ChatGroup group, final String message, final LocalDateTime date) {
        final Message msg = new Message();
        msg.setCreator(user);
        msg.setCreated(date);
        msg.setGroup(group);
        msg.setMessage(message);
        entityManager.persist(msg);

        return msg;
    }

    private ChatGroup createChatGroup(final User user) {
        final ChatGroup group = new ChatGroup();
        group.setCreated(LocalDateTime.now());
        group.setCreator(user);
        entityManager.persist(group);

        return group;
    }

    @Before
    public void init() {
        final User user = createUser("mhenro", true, 0);
        final ChatGroup group1 = createChatGroup(user);
        final LocalDateTime date2 = LocalDateTime.now();
        final LocalDateTime date1 = date2.minusDays(1);
        final LocalDateTime date3 = date2.plusDays(1);
        final Message msg1 = createMessage(user, group1, "msg #1", date1);
        final Message msg2 = createMessage(user, group1, "msg #2", date2);
        final Message msg3 = createMessage(user, group1, "msg #3", date3);
        group1.getMessages().add(msg1);
        group1.getMessages().add(msg2);
        group1.getMessages().add(msg3);
        user.getChatGroups().add(group1);


        final ChatGroup group2 = createChatGroup(user);
        final LocalDateTime date12 = LocalDateTime.now();
        final LocalDateTime date11 = date12.minusDays(1);
        final LocalDateTime date13 = date12.plusDays(1);
        final Message msg11 = createMessage(user, group2, "msg #11", date11);
        final Message msg12 = createMessage(user, group2, "msg #12", date12);
        final Message msg13 = createMessage(user, group2, "msg #13", date13);
        group2.getMessages().add(msg11);
        group2.getMessages().add(msg12);
        group2.getMessages().add(msg13);
        user.getChatGroups().add(group2);


        entityManager.flush();
    }

    @Test
    public void getMessagesByGroup() throws Exception {
        Page<MessageResponse> messages = messageRepository.getMessagesByGroup(1, null);
        Assert.assertEquals(3, messages.getTotalElements());
        Assert.assertEquals("first last", messages.getContent().get(0).getCreatorFullName());
        Assert.assertEquals("msg #1", messages.getContent().get(0).getMessage());
        Assert.assertEquals("msg #2", messages.getContent().get(1).getMessage());
        Assert.assertEquals("msg #3", messages.getContent().get(2).getMessage());
    }
}
