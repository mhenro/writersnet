package org.booklink.services;

import liquibase.util.file.FilenameUtils;
import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.AvatarRequest;
import org.booklink.models.top_models.*;
import org.booklink.repositories.AuthorRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by mhenr on 14.11.2017.
 */
@Service
public class AuthorService {
    private Environment env;
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(Environment env, AuthorRepository authorRepository) {
        this.env = env;
        this.authorRepository = authorRepository;
    }

    public Page<User> getAuthors(final Pageable pageable) {
        Page<User> authors = authorRepository.findAllEnabled(pageable);
        authors.forEach(author -> {
            hideAuthInfo(author);
            removeRecursionFromAuthor(author);
            setDefaultAvatar(author);
            calcBookSize(author);
            hideText(author);
        });
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

    public User getAuthor(final String authorId) {
        final User author = authorRepository.findOne(authorId);
        if (author != null) {
            increaseAuthorViews(author);
            hideAuthInfo(author);
            removeRecursionFromAuthor(author);
            setDefaultAvatar(author);
            setDefaultCoverForBooks(author);
            calcBookSize(author);
            hideText(author);
        }
        return author;
    }

    public void saveAuthor(final User author) {
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
    }

    public void saveAvatar(final AvatarRequest avatarRequest) throws IOException{
        checkCredentials(avatarRequest.getUserId()); //only the owner can change his avatar

        User author = authorRepository.findOne(avatarRequest.getUserId());
        if (author == null) {
            throw new ObjectNotFoundException("Author not found");
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
    }

    public void addUserToFriend() {

    }

    private void increaseAuthorViews(final User author) {
        if (author != null) {
            final long views = author.getViews() + 1;
            author.setViews(views);
            authorRepository.save(author);
        }
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }

    private void hideAuthInfo(User user) {
        user.setPassword("");
        user.setActivationToken("");
        user.setAuthority("");
    }

    private void removeRecursionFromAuthor(User user){
        user.getBooks().stream().forEach(book -> {
            book.setAuthor(null);
        });
        if (user.getSection() != null) {
            user.getSection().setAuthor(null);
        }
    }

    private void setDefaultAvatar(User user) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        if (user.getAvatar() == null) {
            user.setAvatar(defaultAvatar);
        }
    }

    private void setDefaultCoverForBooks(User user) {
        final String defaultCover = env.getProperty("writersnet.coverwebstorage.path") + "default_cover.png";
        user.getBooks().stream()
                .filter(book -> book.getCover() == null || book.getCover().isEmpty())
                .forEach(book -> book.setCover(defaultCover));
    }

    private void calcBookSize(User user) {
        user.getBooks().stream().forEach(book -> {
            int size = Optional.ofNullable(book.getBookText()).map(bookText -> bookText.getText().length()).orElse(0);
            book.setSize(size);
        });

    }

    private void hideText(User user) {
        user.getBooks().stream().forEach(book -> book.setBookText(null));
    }
}