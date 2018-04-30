package com.writersnets.repositories;

import com.writersnets.config.RootConfigTest;
import com.writersnets.models.entities.*;
import com.writersnets.models.response.AuthorShortInfoResponse;
import com.writersnets.models.response.ChatGroupResponse;
import com.writersnets.models.top_models.TopAuthorBookCount;
import com.writersnets.models.top_models.TopAuthorRating;
import com.writersnets.models.top_models.TopAuthorViews;
import org.junit.Assert;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    private User createUser(final String username, final boolean enabled, final long views) {
        final User user = new User();
        user.setUsername(username);
        user.setEnabled(enabled);
        user.setViews(views);
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

    private Comment createComment(final User user, final Book book) {
        final Comment bookComments = new Comment();
        bookComments.setUser(user);
        bookComments.setBook(book);
        bookComments.setComment("comment");
        entityManager.persist(bookComments);

        return bookComments;
    }

    private Rating createRating(final Book book, final int estimation, final String ip) {
        final Rating rating = new Rating();
        final RatingId ratingId = new RatingId();
        ratingId.setClientIp(ip);
        ratingId.setEstimation(estimation);
        ratingId.setBook(book);
        rating.setRatingId(ratingId);
        entityManager.persist(rating);

        return rating;
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
        group.setAvatar("avatar");
        group.setName("name");
        group.setPrimaryRecipient(user);
        entityManager.persist(group);

        return group;
    }

    public void init() {
        createUser("mhenro", true, 0);
        createUser("zazaka", false, 0);

        entityManager.flush();
    }

    public void initSubscriber() {
        final User user1 = createUser("mhenro", true, 9);
        final User user2 = createUser("zazaka", false, 0);
        /*final Friendship friendship = new Friendship();
        final FriendshipPK friendshipPK = new FriendshipPK();
        friendship.setFriendshipPK(friendshipPK);
        friendship.getFriendshipPK().setSubscriber(user1);
        friendship.getFriendshipPK().setSubscription(user2);*/
        //user1.getSubscribers().add(friendship);

        entityManager.flush();
    }

    public void initSubscription() {
        final User user1 = createUser("mhenro", true, 0);
        final User user2 = createUser("zazaka", false, 0);
        /*final Friendship friendship = new Friendship();
        final FriendshipPK friendshipPK = new FriendshipPK();
        friendship.setFriendshipPK(friendshipPK);
        friendship.getFriendshipPK().setSubscriber(user2);
        friendship.getFriendshipPK().setSubscription(user1);*/
        //user1.getSubscriptions().add(friendship);

        entityManager.flush();
    }

    public void initFriend() {
        final User user1 = createUser("mhenro", true, 0);
        final User user2 = createUser("zazaka", false, 0);
        /*final Friendship friendshipSubscribe = new Friendship();
        final FriendshipPK friendshipPKSubscribe = new FriendshipPK();
        friendshipSubscribe.setFriendshipPK(friendshipPKSubscribe);
        friendshipSubscribe.getFriendshipPK().setSubscriber(user1);
        friendshipSubscribe.getFriendshipPK().setSubscription(user2);*/
        //user1.getSubscribers().add(friendshipSubscribe);

        /*final Friendship friendshipSubscription = new Friendship();
        final FriendshipPK friendshipPKSubscription = new FriendshipPK();
        friendshipSubscription.setFriendshipPK(friendshipPKSubscription);
        friendshipSubscription.getFriendshipPK().setSubscriber(user2);
        friendshipSubscription.getFriendshipPK().setSubscription(user1);*/
        //user1.getSubscriptions().add(friendshipSubscription);

        entityManager.flush();
    }

    private void initChatGroups() {
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

    private void initForTops() {
        entityManager.clear();
        final User userPrev = createUser("prev", true, 1);
        final User userCurrent = createUser("current", true, 2);
        final User userNext = createUser("next", true, 3);
        final Book bookPrev = createBook(userPrev);
        final Book bookCurrent1 = createBook(userCurrent);
        final Book bookCurrent2 = createBook(userCurrent);
        final Book bookNext1 = createBook(userNext);
        final Book bookNext2 = createBook(userNext);
        final Book bookNext3 = createBook(userNext);
        createComment(userPrev, bookPrev);
        createComment(userCurrent, bookCurrent1);
        createComment(userCurrent, bookCurrent2);
        createComment(userNext, bookNext1);
        createComment(userNext, bookNext2);
        createComment(userNext, bookNext3);
        createRating(bookPrev, 3, "127.0.0.1");
        createRating(bookPrev, 4, "127.0.0.2");
        createRating(bookPrev, 4, "127.0.0.3");
        createRating(bookCurrent1, 4, "127.0.0.1");
        createRating(bookCurrent2, 5, "127.0.0.1");
        createRating(bookCurrent2, 5, "127.0.0.2");
        createRating(bookNext1, 2, "127.0.0.1");
        createRating(bookNext1, 2, "127.0.0.2");
        createRating(bookNext1, 2, "127.0.0.3");
        createRating(bookNext2, 4, "127.0.0.1");
        createRating(bookNext2, 4, "127.0.0.2");
        createRating(bookNext2, 4, "127.0.0.3");
        createRating(bookNext3, 4, "127.0.0.1");
        createRating(bookNext3, 4, "127.0.0.2");
        createRating(bookNext3, 5, "127.0.0.3");

        entityManager.flush();
    }

    @Test
    public void findAllEnable() throws Exception {
        init();
        Page<AuthorShortInfoResponse> users = authorRepository.findAllEnabled(null);
        Assert.assertEquals(1, users.getTotalElements());
        Assert.assertEquals("mhenro", users.getContent().get(0).getUsername());
    }

    @Test
    public void findAllByRating() throws Exception {
        initForTops();
        Page<TopAuthorRating> users = authorRepository.findAllByRating(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(31, users.getContent().get(0).getTotalEstimation());
        Assert.assertEquals(9, users.getContent().get(0).getTotalVotes());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(14, users.getContent().get(1).getTotalEstimation());
        Assert.assertEquals(3, users.getContent().get(1).getTotalVotes());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(11, users.getContent().get(2).getTotalEstimation());
        Assert.assertEquals(3, users.getContent().get(2).getTotalVotes());
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
    public void findAllByComments() throws Exception {
        /*initForTops();
        Page<TopAuthorComments> users = authorRepository.findAllByComments(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(3, users.getContent().get(0).getCommentsCount());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(2, users.getContent().get(1).getCommentsCount());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(1, users.getContent().get(2).getCommentsCount());*/
    }

    @Test
    public void findAllByViews() throws Exception {
        initForTops();
        Page<TopAuthorViews> users = authorRepository.findAllByViews(null);
        Assert.assertEquals(3, users.getTotalElements());
        Assert.assertEquals("next", users.getContent().get(0).getUsername());
        Assert.assertEquals(3, users.getContent().get(0).getViews());
        Assert.assertEquals("current", users.getContent().get(1).getUsername());
        Assert.assertEquals(2, users.getContent().get(1).getViews());
        Assert.assertEquals("prev", users.getContent().get(2).getUsername());
        Assert.assertEquals(1, users.getContent().get(2).getViews());
    }

    @Test
    public void findOne() throws Exception {
        init();
        User user = authorRepository.findById("mhenro").orElse(null);
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

    @Test
    public void subscribeOnUser_subscriber() throws Exception {
        initSubscriber();
        final User user = authorRepository.findById("mhenro").orElse(null);
        /*Assert.assertEquals(true, user.isSubscriberOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriberOf("mhenro"));
        Assert.assertEquals(false, user.isSubscriptionOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriptionOf("mhenro"));
        Assert.assertEquals(false, user.isFriendOf("zazaka"));
        Assert.assertEquals(false, user.isFriendOf("mhenro"));*/
    }

    @Test
    public void subscribeOnUser_subscription() throws Exception {
        initSubscription();
        final User user = authorRepository.findById("mhenro").orElse(null);
        /*Assert.assertEquals(true, user.isSubscriptionOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriptionOf("mhenro"));
        Assert.assertEquals(false, user.isSubscriberOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriberOf("mhenro"));
        Assert.assertEquals(false, user.isFriendOf("zazaka"));
        Assert.assertEquals(false, user.isFriendOf("mhenro"));*/
    }

    @Test
    public void subscribeOnUser_friend() throws Exception {
        initFriend();
        final User user = authorRepository.findById("mhenro").orElse(null);
        /*Assert.assertEquals(true, user.isFriendOf("zazaka"));
        Assert.assertEquals(false, user.isFriendOf("mhenro"));
        Assert.assertEquals(true, user.isSubscriptionOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriptionOf("mhenro"));
        Assert.assertEquals(true, user.isSubscriberOf("zazaka"));
        Assert.assertEquals(false, user.isSubscriberOf("mhenro"));*/
    }

    @Test
    public void getChatGroups() throws Exception {
        initChatGroups();
        final Page<ChatGroupResponse> response = authorRepository.getChatGroups("mhenro", null);
        Assert.assertNotNull(response);
        Assert.assertEquals(2, response.getTotalElements());
        Assert.assertEquals(1, response.getContent().get(0).getId());
        Assert.assertEquals("mhenro", response.getContent().get(0).getCreatorId());
        Assert.assertEquals("msg #3", response.getContent().get(0).getLastMessageText());
        Assert.assertEquals("mhenro", response.getContent().get(1).getCreatorId());
        Assert.assertEquals("msg #13", response.getContent().get(1).getLastMessageText());
    }
}
