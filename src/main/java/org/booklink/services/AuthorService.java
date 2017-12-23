package org.booklink.services;

import liquibase.util.file.FilenameUtils;
import org.booklink.models.Response;
import org.booklink.models.entities.Friendship;
import org.booklink.models.entities.FriendshipPK;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.AuthorRequest;
import org.booklink.models.request.AvatarRequest;
import org.booklink.models.response.*;
import org.booklink.models.top_models.*;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.FriendshipRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Created by mhenr on 14.11.2017.
 */
@Service
public class AuthorService {
    private Environment env;
    private AuthorRepository authorRepository;
    private FriendshipRepository friendshipRepository;
    private NewsService newsService;

    @Autowired
    public AuthorService(final Environment env, final AuthorRepository authorRepository, final FriendshipRepository friendshipRepository, final NewsService newsService) {
        this.env = env;
        this.authorRepository = authorRepository;
        this.friendshipRepository = friendshipRepository;
        this.newsService = newsService;
    }

    public Page<AuthorShortInfoResponse> getAuthors(final Pageable pageable) {
        Page<AuthorShortInfoResponse> authors = authorRepository.findAllEnabled(pageable);
        authors.forEach(this::setDefaultAvatar);
        return authors;
    }

    public Page<AuthorShortInfoResponse> getAuthorsByName(final String name, final Pageable pageable) {
        Page<AuthorShortInfoResponse> authors = authorRepository.findAuthorsByName(name, pageable);
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
        Page<TopAuthorComments> authors = null; //authorRepository.findAllByComments(pageable);     //TODO: fixme!
        return authors;
    }

    public Page<TopAuthorViews> getAuthorsByViews(final Pageable pageable) {
        Page<TopAuthorViews> authors = authorRepository.findAllByViews(pageable);
        return authors;
    }

    public AuthorResponse getAuthor(final String authorId) {
        final AuthorResponse author = authorRepository.findAuthor(authorId);
        setDefaultAvatar(author);
        increaseAuthorViews(authorId);
        return author;
    }

    public boolean isFriendOf(final String authorId) {
        final User authorizedUser = getAuthorizedUser();
        return authorizedUser.isFriendOf(authorId);
    }

    public boolean isSubscriberOf(final String authorId) {
        final User authorizedUser = getAuthorizedUser();
        return authorizedUser.isSubscriberOf(authorId);
    }

    public boolean isSubscription(final String authorId) {
        final User authorizedUser = getAuthorizedUser();
        return authorizedUser.isSubscriptionOf(authorId);
    }

    public CheckFriendshipResponse checkFriendshipWith(final String authorId) {
        final User authorizedUser = getAuthorizedUser();
        final boolean friend = authorizedUser.isFriendOf(authorId);
        final boolean subscriber = authorizedUser.isSubscriberOf(authorId);
        final boolean subscription = authorizedUser.isSubscriptionOf(authorId);
        final CheckFriendshipResponse response = new CheckFriendshipResponse(friend, subscriber, subscription);
        return response;
    }

    public void updateAuthor(final AuthorRequest author) {
        checkCredentials(author.getUsername());
        User user = authorRepository.findOne(author.getUsername());
        if (user == null) {
            throw new ObjectNotFoundException();
        }
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

    public void saveAvatar(final AvatarRequest avatarRequest) throws IOException{
        checkCredentials(avatarRequest.getUserId()); //only the owner can change his avatar

        User author = authorRepository.findOne(avatarRequest.getUserId());
        if (author == null) {
            throw new ObjectNotFoundException("Author is not found");
        }
        checkCredentials(author.getUsername()); //only the owner can change his avatar
        String uploadDir = env.getProperty("writersnet.avatarstorage.path");
        File file = new File(uploadDir);
        if (!file.exists()) {
            file.mkdir();
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

    public Response<String> subscribeOnUser(final String subscriptionId) {
        Response<String> response = new Response<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        User user = authorRepository.findOne(currentUser);
        if (user == null) {
            throw new ObjectNotFoundException("Author is not found");
        }
        User subscriptionUser = authorRepository.findOne(subscriptionId);
        if (subscriptionUser == null) {
            throw new ObjectNotFoundException("Subscription is not found");
        }
        Friendship subscription = new Friendship();
        subscription.setActive(true);
        subscription.setDate(new Date());
        FriendshipPK friendshipPK = new FriendshipPK();
        subscription.setFriendshipPK(friendshipPK);
        subscription.getFriendshipPK().setSubscriber(user);
        subscription.getFriendshipPK().setSubscription(subscriptionUser);
        friendshipRepository.save(subscription);
        user.getSubscriptions().add(subscription);
        authorRepository.save(user);

        response.setCode(0);
        if (user.isFriendOf(subscriptionId)) {
            response.setMessage("You has added " + subscriptionUser.getFullName() + " to the friend list");
        } else if (user.isSubscriberOf(subscriptionId)) {
            response.setMessage("You has added " + subscriptionUser.getFullName() + " to your subscriptions");
        }

        newsService.createNews(NewsService.NEWS_TYPE.FRIEND_ADDED, user, subscriptionUser);

        return response;
    }

    public Response<String> removeSubscription(final String subscriptionId) {
        Response<String> response = new Response<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        User user = authorRepository.findOne(currentUser);
        if (user == null) {
            throw new ObjectNotFoundException("Author is not found");
        }
        User subscriptionUser = authorRepository.findOne(subscriptionId);
        if (subscriptionUser == null) {
            throw new ObjectNotFoundException("Subscription is not found");
        }
        response.setCode(0);
        if (user.isFriendOf(subscriptionId)) {
            response.setMessage(subscriptionUser.getFullName() + " was moved to your subscribers");
        } else if (user.isSubscriberOf(subscriptionId)) {
            response.setMessage(subscriptionUser.getFullName() + " was removed from your subscriptions");
        }
        Set<Friendship> friendships = user.getSubscribers();
        Friendship friendship = friendships.stream().filter(friend -> friend.getSubscriptionId().equals(subscriptionId)).findAny().orElseGet(() -> null);
        friendshipRepository.delete(friendship);

        return response;
    }

    public Page<ChatGroupResponse> getChatGroups(final String userId, final Pageable pageable) {
        Page<ChatGroupResponse> groups = authorRepository.getChatGroups(userId, pageable);
        return groups;
    }

    public Page<FriendResponse> getFriends(final String userId, final String matcher, final Pageable pageable) {
        checkCredentials(userId);
        Page<FriendResponse> friends = authorRepository.getFriends(userId, matcher, pageable);
        return friends;
    }

    private void increaseAuthorViews(final String authorId) {
        final User author = authorRepository.findOne(authorId);
        if (author != null) {
            final long views = author.getViews() + 1;
            author.setViews(views);
            authorRepository.save(author);
        }
    }

    private void checkCredentials(final String userId) {
        if (!getAuthorizedUser().getUsername().equals(userId)) {
            throw new UnauthorizedUserException("Bad credentials");
        }
    }

    private User getAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        final User user = authorRepository.findOne(currentUser);
        if (user == null) {
            throw new UnauthorizedUserException("User not found");
        }
        return user;
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
        if (user != null) {
            final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
            if (user.getAvatar() == null) {
                user.setAvatar(defaultAvatar);
            }
        }
    }
}