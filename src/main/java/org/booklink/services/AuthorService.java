package org.booklink.services;

import org.booklink.models.Response;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
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
            calcBookSize(author);
            hideText(author);
        });
        return authors;
    }

    public User getAuthor(final String authorId) {
        final User author = authorRepository.findOne(authorId);
        if (author != null) {
            hideAuthInfo(author);
            removeRecursionFromAuthor(author);
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