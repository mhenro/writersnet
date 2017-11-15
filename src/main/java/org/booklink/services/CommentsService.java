package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookComments;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.BookComment;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookCommentsRepository;
import org.booklink.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
public class CommentsService {
    private Environment env;
    private BookCommentsRepository bookCommentsRepository;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    @Autowired
    public CommentsService(final Environment env,
                           final BookCommentsRepository bookCommentsRepository,
                           final BookRepository bookRepository,
                           final AuthorRepository authorRepository) {
        this.env = env;
        this.bookCommentsRepository = bookCommentsRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<BookComments> getComments(final Long bookId, final Pageable pageable) {
        Page<BookComments> comments = bookCommentsRepository.findAllByBookId(bookId, pageable);
        setDefaultAvatars(comments);
        return comments;
    }

    public void saveComment(final BookComment bookComment) {
        BookComments entity = new BookComments();
        Book book = bookRepository.findOne(bookComment.getBookId());
        if (book == null) {
            throw new ObjectNotFoundException("Book was not found");
        }
        if (bookComment.getUserId() != null) {
            User user = authorRepository.findOne(bookComment.getUserId());
            if (user == null) {
                throw new ObjectNotFoundException("Author was not found");
            }
            entity.setUser(user);
        }
        entity.setBook(book);
        entity.setComment(bookComment.getComment());
        entity.setRelatedTo(bookComment.getRelatedTo());
        entity.setCreated(new Date());
        bookCommentsRepository.save(entity);
    }

    public void deleteComment(final Long bookId, final Long commentId) {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new ObjectNotFoundException("Book was not found");
        }
        checkCredentials(book.getAuthor().getUsername());   //only owner can delete comments from his book
        bookCommentsRepository.delete(commentId);
    }

    private void setDefaultAvatars(final Page<BookComments> comments) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        comments.getContent().stream()
                .filter(comment -> comment.getAuthorInfo().getAvatar() == null || comment.getAuthorInfo().getAvatar().isEmpty())
                .forEach(comment -> comment.getAuthorInfo().setAvatar(defaultAvatar));
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
