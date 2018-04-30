package com.writersnets.services;

import com.writersnets.models.entities.Book;
import com.writersnets.models.entities.Comment;
import com.writersnets.models.entities.User;
import com.writersnets.models.exceptions.ObjectNotFoundException;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.models.exceptions.WrongDataException;
import com.writersnets.models.request.CommentRequest;
import com.writersnets.models.response.CommentResponse;
import com.writersnets.models.response.DetailedCommentResponse;
import com.writersnets.repositories.AuthorRepository;
import com.writersnets.repositories.BookCommentsRepository;
import com.writersnets.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mhenr on 15.11.2017.
 */
@Service
@Transactional(readOnly = true)
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

    public Page<CommentResponse> getComments(final Long bookId, final Pageable pageable) {
        Page<CommentResponse> comments = bookCommentsRepository.findAllByBookId(bookId, pageable);
        if (comments != null) {
            setDefaultAvatars(comments);
        }
        return comments;
    }

    public Page<DetailedCommentResponse> getCommentsGroupByBookOrderByDate(final Pageable pageable) {
        Page<DetailedCommentResponse> comments = bookCommentsRepository.getCommentsGroupByBookOrderByDate(pageable);
        if (comments != null) {
            setDefaultAvatarsAndCovers(comments);
        }
        return comments;
    }

    @Transactional
    public void saveComment(final CommentRequest bookComment) {
        if (bookComment.getComment() == null || bookComment.getComment().isEmpty()) {
            throw new WrongDataException("Comment must not be empty");
        }
        Comment entity = new Comment();
        Book book = bookRepository.findById(bookComment.getBookId()).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        if (bookComment.getUserId() != null) {
            User user = authorRepository.findById(bookComment.getUserId()).orElseThrow(() -> new ObjectNotFoundException("Author is not found"));
            entity.setUser(user);
            newsService.createNews(NewsService.NEWS_TYPE.COMMENT, user, book);
        }
        entity.setBook(book);
        entity.setComment(bookComment.getComment());
        entity.setCreated(LocalDateTime.now());
        if (bookComment.getRelatedTo() != null) {
            final Comment relatedComment = bookCommentsRepository.findById(bookComment.getRelatedTo()).orElseThrow(() -> new ObjectNotFoundException("Related comment is not found"));
            entity.setRelatedTo(relatedComment);
        }
        increaseCommentsInBookAndUser(entity);
        bookCommentsRepository.save(entity);
    }

    @Transactional
    public void deleteComment(final Long bookId, final Long commentId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ObjectNotFoundException("Book is not found"));
        checkCredentials(book.getAuthor().getUsername());   //only owner can delete comments from his book
        decreaseCommentsInBookAndUser(commentId);
        bookCommentsRepository.deleteById(commentId);
    }

    private void setDefaultAvatars(final Page<CommentResponse> comments) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        comments.getContent().stream()
                .filter(comment -> comment.getUserAvatar() == null || comment.getUserAvatar().isEmpty())
                .forEach(comment -> comment.setUserAvatar(defaultAvatar));
    }

    private void setDefaultAvatarsAndCovers(final Page<DetailedCommentResponse> comments) {
        final String defaultAvatar = env.getProperty("writersnet.avatarwebstorage.path") + "default_avatar.png";
        final String defaultCover = env.getProperty("writersnet.coverwebstorage.path") + "default_cover.png";
        comments.getContent().stream()
                .filter(comment -> comment.getUserAvatar() == null || comment.getUserAvatar().isEmpty())
                .forEach(comment -> comment.setUserAvatar(defaultAvatar));
        comments.getContent().stream()
                .filter(comment -> comment.getBookCover() == null || comment.getBookCover().isEmpty())
                .forEach(comment -> comment.setBookCover(defaultCover));
    }

    private void increaseCommentsInBookAndUser(final Comment comment) {
        if (comment.getBook() != null) {
            final Long bookComments = comment.getBook().getCommentsCount() + 1;
            comment.getBook().setCommentsCount(bookComments);
        }
        if (comment.getUser() != null) {
            comment.getUser().refreshCommentsCountFromBooks();
        }
    }

    private void decreaseCommentsInBookAndUser(final Long commentId) {
        bookCommentsRepository.findById(commentId)
                .ifPresent(comment -> decreaseCommentsInBookAndUser(comment));
    }

    private void decreaseCommentsInBookAndUser(final Comment comment) {
        if (comment.getBook() != null) {
            final Long bookComments = comment.getBook().getCommentsCount() > 0 ? comment.getBook().getCommentsCount() - 1 : 0;
            comment.getBook().setCommentsCount(bookComments);
        }
        if (comment.getUser() != null) {
            comment.getUser().refreshCommentsCountFromBooks();
        }
    }

    private void checkCredentials(final String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(userId)) {
            throw new UnauthorizedUserException("Bad credentials");
        }
    }
}
