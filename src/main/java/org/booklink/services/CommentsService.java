package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.BookComments;
import org.booklink.models.entities.User;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.BookComment;
import org.booklink.models.response.BookCommentResponse;
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
    private NewsService newsService;

    @Autowired
    public CommentsService(final Environment env,
                           final BookCommentsRepository bookCommentsRepository,
                           final BookRepository bookRepository,
                           final AuthorRepository authorRepository,
                           final NewsService newsService) {
        this.env = env;
        this.bookCommentsRepository = bookCommentsRepository;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.newsService = newsService;
    }

    public Page<BookCommentResponse> getComments(final Long bookId, final Pageable pageable) {
        Page<BookCommentResponse> comments = bookCommentsRepository.findAllByBookId(bookId, pageable);
        if (comments != null) {
            setDefaultAvatars(comments);
        }
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
            newsService.createNews(NewsService.NEWS_TYPE.COMMENT, user, book);
        }
        entity.setBook(book);
        entity.setComment(bookComment.getComment());
        entity.setCreated(new Date());
        if (bookComment.getRelatedTo() != null) {
            final BookComments relatedComment = bookCommentsRepository.findOne(bookComment.getRelatedTo());
            if (relatedComment == null) {
                throw new ObjectNotFoundException("Related comment was not found");
            }
            entity.setRelatedTo(relatedComment);
        }
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

    private void setDefaultAvatars(final Page<BookCommentResponse> comments) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        comments.getContent().stream()
                .filter(comment -> comment.getUserAvatar() == null || comment.getUserAvatar().isEmpty())
                .forEach(comment -> comment.setUserAvatar(defaultAvatar));
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException();
        }
    }
}
