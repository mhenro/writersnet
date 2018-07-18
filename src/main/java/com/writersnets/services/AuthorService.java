package com.writersnets.services;

//import liquibase.util.file.FilenameUtils;
import com.writersnets.models.Response;
import com.writersnets.models.entities.User;
import com.writersnets.models.exceptions.IsNotPremiumUserException;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.request.AuthorRequest;
import com.writersnets.models.request.AvatarRequest;
import com.writersnets.models.request.ChangePasswordRequest;
import com.writersnets.models.response.*;
import com.writersnets.models.top_models.*;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.FriendshipRepository;
import com.writersnets.utils.ObjectHelper;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

/**
 * Created by mhenr on 14.11.2017.
 */
@Service
@Transactional(readOnly = true)
public class AuthorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Environment env;
    private AuthorRepository authorRepository;
    private FriendshipRepository friendshipRepository;
    private NewsService newsService;
    private AuthorizedUserService authorizedUserService;

    @Autowired
    public AuthorService(final Environment env, final AuthorRepository authorRepository,
                         final FriendshipRepository friendshipRepository, final NewsService newsService,
                         final AuthorizedUserService authorizedUserService) {
        this.env = env;
        this.authorRepository = authorRepository;
        this.friendshipRepository = friendshipRepository;
        this.newsService = newsService;
        this.authorizedUserService = authorizedUserService;
    }

    public Page<AuthorShortInfoResponse> getAuthors(final Pageable pageable) {
        Page<AuthorShortInfoResponse> authors;
        if (pageable.getSort() != null) {
            switch (pageable.getSort().toString().split(":")[0]) {
                case "totalRating":
                    authors = authorRepository.findAllEnabledSortByRating(pageable);
                    break;
                case "online":
                    authors = authorRepository.findAllEnabledSortByOnline(pageable);
                    break;
                default:
                    authors = authorRepository.findAllEnabledSortByName(pageable);
            }
        } else {
            authors = authorRepository.findAllEnabledSortByName(pageable);
        }
        authors.forEach(this::setDefaultAvatar);
        return authors;
    }

    public Long getAuthorsCount() {
        return authorRepository.getAuthorsCount();
    }

    public Page<AuthorShortInfoResponse> getAuthorsByName(final String name, final Pageable pageable) {
        Page<AuthorShortInfoResponse> authors;
        if (pageable.getSort() != null) {
            switch (pageable.getSort().toString().split(":")[0]) {
                case "totalRating":
                    authors = authorRepository.findAuthorsByNameSortByRating(name, pageable);
                    break;
                case "online":
                    authors = authorRepository.findAuthorsByNameSortByOnline(name, pageable);
                    break;
                default:
                    authors = authorRepository.findAuthorsByNameSortByName(name, pageable);
            }
        } else {
            authors = authorRepository.findAuthorsByNameSortByName(name, pageable);
        }
        authors.forEach(this::setDefaultAvatar);
        return authors;
    }

    public Page<TopAuthorRating> getAuthorsByRating(final Pageable pageable) {
        Page<TopAuthorRating> authors = authorRepository.findAllByRating(pageable);
        return authors;
    }

    public Page<TopAuthorBookCount> getAuthorsByBookCount(final Pageable pageable) {
        Page<TopAuthorBookCount> authors = authorRepository.findAllByBookCount(pageable);
        return authors;
    }

    public Page<TopAuthorComments> getAuthorsByComments(final Pageable pageable) {
        Page<TopAuthorComments> authors = authorRepository.findAllByComments(pageable);
        return authors;
    }

    public Page<TopAuthorViews> getAuthorsByViews(final Pageable pageable) {
        Page<TopAuthorViews> authors = authorRepository.findAllByViews(pageable);
        return authors;
    }

    @Transactional
    public AuthorResponse getAuthor(final String authorId) {
        final AuthorResponse author = authorRepository.findAuthor(authorId);
        if (author == null) {
            throw new ObjectNotFoundException("Author not found");
        }
        setDefaultAvatar(author);
        increaseAuthorViews(authorId);
        return author;
    }

    public boolean isFriendOf(final String authorId) {
        final User authorizedUser = authorizedUserService.getAuthorizedUser();
        final FriendshipResponse friend = friendshipRepository.getFriendByName(authorizedUser.getUsername(), authorId);
        return Optional.ofNullable(friend).map(element -> true).orElse(false);
    }

    public boolean isSubscriberOf(final String authorId) {
        final User authorizedUser = authorizedUserService.getAuthorizedUser();
        final FriendshipResponse subscriber = friendshipRepository.getSubscriberByName(authorizedUser.getUsername(), authorId);
        return Optional.ofNullable(subscriber).map(element -> true).orElse(false);
    }

    public boolean isSubscriptionOf(final String authorId) {
        final User authorizedUser = authorizedUserService.getAuthorizedUser();
        final FriendshipResponse subscription = friendshipRepository.getSubscriptionByName(authorizedUser.getUsername(), authorId);
        return Optional.ofNullable(subscription).map(element -> true).orElse(false);
    }

    public CheckFriendshipResponse checkFriendshipWith(final String authorId) {
        final boolean friend = isFriendOf(authorId);
        final boolean subscriber = isSubscriberOf(authorId);
        final boolean subscription = isSubscriptionOf(authorId);
        final CheckFriendshipResponse response = new CheckFriendshipResponse(friend, subscriber, subscription);
        return response;
    }

    @Transactional
    public void updateAuthor(final AuthorRequest author) {
        checkCredentials(author.getUsername());
        User user = authorRepository.findById(author.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User is not found"));
        BeanUtils.copyProperties(author, user, ObjectHelper.getNullPropertyNames(author));
        if (author.getSectionName() != null) {
            user.getSection().setName(author.getSectionName());
        }
        if (author.getSectionDescription() != null) {
            user.getSection().setDescription(author.getSectionDescription());
        }
        authorRepository.save(user);
        newsService.createNews(NewsService.NEWS_TYPE.PERSONAL_INFO_UPDATED, user);
    }

    @Transactional
    public void changePassword(final ChangePasswordRequest request) {
        final User user = authorizedUserService.getAuthorizedUser();
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UnauthorizedUserException("Your current password is incorrect");
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new UnauthorizedUserException("Your new password doesn't equals to your password confirmation");
        }
        final String password = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(password);
    }

    @Transactional
    public void saveAvatar(final AvatarRequest avatarRequest) throws IOException {
        checkCredentials(avatarRequest.getUserId()); //only the owner can change his avatar

        User author = authorRepository.findById(avatarRequest.getUserId()).orElseThrow(() -> new ObjectNotFoundException("Author is not found"));
        if (avatarRequest.getAvatar().getSize() >= 102400 && !author.getPremium()) {    //only premium users can have avatars larger than 100Kb
            throw new IsNotPremiumUserException("Only a premium user can add the avatar larger than 100 Kb");
        }
        checkCredentials(author.getUsername()); //only the owner can change his avatar
        String uploadDir = env.getProperty("writersnet.avatarstorage.path");
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String originalName = avatarRequest.getUserId().toString() + "." + FilenameUtils.getExtension(avatarRequest.getAvatar().getOriginalFilename());

        String filePath = uploadDir + originalName;
        File dest = new File(filePath);
        avatarRequest.getAvatar().transferTo(dest);

        String avatarLink = env.getProperty("writersnet.avatarwebstorage.path") + originalName;
        author.setAvatar(avatarLink);
        authorRepository.save(author);
        newsService.createNews(NewsService.NEWS_TYPE.PERSONAL_INFO_UPDATED, author);
    }

    @Transactional
    public void restoreDefaultAvatar() {
        final User authorizedUser = authorizedUserService.getAuthorizedUser();
        authorizedUser.setAvatar(null);
        newsService.createNews(NewsService.NEWS_TYPE.PERSONAL_INFO_UPDATED, authorizedUser);
    }

    @Transactional
    public Response<String> subscribeOnUser(final String subscriptionId) {
        Response<String> response = new Response<>();
        response.setCode(0);
        final String authorizedUser = authorizedUserService.getAuthorizedUser().getUsername();
        final User subscriptionUser = authorRepository.findById(subscriptionId).orElseThrow(() -> new ObjectNotFoundException("Subscription is not found"));
        final boolean isFriend = isFriendOf(subscriptionId);
        final boolean isSubscriber = isSubscriberOf(subscriptionId);
        final boolean isSubscription = isSubscriptionOf(subscriptionId);
        newsService.createNews(NewsService.NEWS_TYPE.FRIEND_ADDED, authorizedUserService.getAuthorizedUser(), subscriptionUser);
        if (isFriend) {
            response.setMessage(subscriptionUser.getFullName() + " has already been added to your friends");
            return response;
        }
        if (isSubscription) {
            response.setMessage(subscriptionUser.getFullName() + " has already been added to your subscriptions");
            return response;
        }
        if (isSubscriber) {
            /* adding user to my friend list */
            friendshipRepository.addToFriends(authorizedUser, subscriptionId, new Date());
            friendshipRepository.removeSubscriber(authorizedUser, subscriptionId);

            /* adding me to his friend list */
            friendshipRepository.addToFriends(subscriptionId, authorizedUser, new Date());
            friendshipRepository.removeSubscription(subscriptionId, authorizedUser);

            response.setMessage(subscriptionUser.getFullName() + " has added to your friend list");
            return response;
        }
        /* adding user to my subscriptions */
        friendshipRepository.addToSubscriptions(authorizedUser, subscriptionId, new Date());

        /* adding me to his subscribers */
        friendshipRepository.addToSubscribers(subscriptionId, authorizedUser, new Date());

        response.setMessage(subscriptionUser.getFullName() + " has been added to your subscriptions");
        return response;
    }

    @Transactional
    public Response<String> removeSubscription(final String subscriptionId) {
        Response<String> response = new Response<>();
        response.setCode(0);
        final String authorizedUser = authorizedUserService.getAuthorizedUser().getUsername();
        final User subscriptionUser = authorRepository.findById(subscriptionId).orElseThrow(() -> new ObjectNotFoundException("Subscription is not found"));
        final boolean isFriend = isFriendOf(subscriptionId);
        final boolean isSubscriber = isSubscriberOf(subscriptionId);
        final boolean isSubscription = isSubscriptionOf(subscriptionId);
        if (isFriend) {
            /* removing user from my friend list */
            friendshipRepository.removeFriend(authorizedUser, subscriptionId);
            friendshipRepository.addToSubscribers(authorizedUser, subscriptionId, new Date());

            /* removing me from his friend list */
            friendshipRepository.removeFriend(subscriptionId, authorizedUser);
            friendshipRepository.addToSubscriptions(subscriptionId, authorizedUser, new Date());

            response.setMessage(subscriptionUser.getFullName() + " has been removed from your friend list and has been added to your subscribers");
            return response;
        }
        if (isSubscriber) {
            response.setCode(1);
            response.setMessage("You cannot remove your subscriber");
            return response;
        }
        if (isSubscription) {
            /* removing user from my subscriptions */
            friendshipRepository.removeSubscription(authorizedUser, subscriptionId);

            /* removing me from his subscribers */
            friendshipRepository.removeSubscriber(subscriptionId, authorizedUser);

            response.setMessage(subscriptionUser.getFullName() + " has been removed from your subscriptions");
            return response;
        }

        response.setCode(1);
        response.setMessage("You don't have a relationship with " + subscriptionUser.getFullName());
        return response;
    }

    public Page<ChatGroupResponse> getChatGroups(final String userId, final Pageable pageable) {
        Page<ChatGroupResponse> groups = authorRepository.getChatGroups(userId, pageable);
        return groups;
    }

    public Page<FriendshipResponse> getAllFriends(final String userId, final Pageable pageable) {
        checkCredentials(userId);
        return friendshipRepository.getAllFriends(userId, pageable);
    }

    public Page<FriendResponse> getAllFriendsByName(final String userId, final String matcher, final Pageable pageable) {
        checkCredentials(userId);
        Page<FriendResponse> friends = friendshipRepository.getAllFriendsByName(userId, matcher, pageable);
        return friends;
    }

    public Long getNewFriendsCount(final String userId) {
        checkCredentials(userId);
        return friendshipRepository.getNewFriendsCount(userId);
    }

    public Page<FriendshipResponse> getAllSubscribers(final String userId, final Pageable pageable) {
        checkCredentials(userId);
        return friendshipRepository.getAllSubscribers(userId, pageable);
    }

    public Page<FriendshipResponse> getAllSubscriptions(final String userId, final Pageable pageable) {
        checkCredentials(userId);
        return friendshipRepository.getAllSubscriptions(userId, pageable);
    }

    @Transactional
    @Scheduled(fixedDelay = 86400000) //1 day
    public void disableExpiredPremiumAccounts() {
       // logger.info("Premium cleared");
        authorRepository.disableExpiredPremiumAccounts(LocalDateTime.now());
    }

    private void increaseAuthorViews(final String authorId) {
        authorRepository.findById(authorId).ifPresent(author -> {
            final long views = author.getViews() + 1;
            author.setViews(views);
            authorRepository.save(author);
        });
    }

    private void checkCredentials(final String userId) {
        if (!authorizedUserService.getAuthorizedUser().getUsername().equals(userId)) {
            throw new UnauthorizedUserException("Bad credentials");
        }
    }

    private void setDefaultAvatar(AuthorShortInfoResponse user) {
        if (user != null) {
            final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
            if (user.getAvatar() == null) {
                user.setAvatar(defaultAvatar);
            }
        }
    }

    private void setDefaultAvatar(AuthorResponse user) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        if (user.getAvatar() == null) {
            user.setAvatar(defaultAvatar);
        }
    }
}