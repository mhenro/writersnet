package org.booklink.controllers;

import org.booklink.models.Genre;
import org.booklink.models.Response;
import org.booklink.models.exceptions.IsNotPremiumUserException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.TextConvertingException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request.BookRequest;
import org.booklink.models.request.BookTextRequest;
import org.booklink.models.request.CoverRequest;
import org.booklink.models.response.BookResponse;
import org.booklink.models.response.BookWithTextResponse;
import org.booklink.services.BookService;
import org.booklink.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.booklink.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
public class BookController {
    private BookService bookService;
    private SessionService sessionService;
    private Environment environment;

    @Autowired
    public BookController(final BookService bookService, final SessionService sessionService, final Environment environment) {
        this.bookService = bookService;
        this.sessionService = sessionService;
        this.environment = environment;
    }

    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.GET)
    public Page<BookResponse> getBooks(final String genre, final String language, final Pageable pageable) {
        return bookService.getBooks(genre, language, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "books/name/{bookName}", method = RequestMethod.GET)
    public Page<BookResponse> getBooksByName(@PathVariable final String bookName, final String genre, final String language, final Pageable pageable) {
        return bookService.getBooksByName(bookName, genre, language, pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "count/books", method = RequestMethod.GET)
    public ResponseEntity<?> getBooksCount() {
        final long count = bookService.getBooksCount();
        return Response.createResponseEntity(0, count, null, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable Long bookId, final Integer page, final Integer size) {
        BookWithTextResponse book = bookService.getBook(bookId, page, size);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "books/author/{authorId}", method = RequestMethod.GET)
    public Page<BookResponse> getBooksByAuthor(@PathVariable final String authorId, final Pageable pageable) {
        return bookService.getBooksByAuthor(authorId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.POST)
    public ResponseEntity<?> saveBook(@RequestBody final BookRequest book) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Long bookId = bookService.saveBook(book);
        return Response.createResponseEntity(0, String.valueOf(bookId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "cover", method = RequestMethod.POST)
    public ResponseEntity<?> saveCover(final CoverRequest coverRequest) throws IOException {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.saveCover(coverRequest);
        return Response.createResponseEntity(0, "Cover was saved successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "cover/restore/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> restoreDefaultCover(@PathVariable final Long bookId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.restoreDefaultCover(bookId);
        return Response.createResponseEntity(0, "Cover was restored successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "text", method = RequestMethod.POST)
    public ResponseEntity<?> saveBookText(final BookTextRequest bookTextRequest) throws IOException {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        final LocalDateTime lastUpdated = bookService.saveBookText(bookTextRequest);
        return Response.createResponseEntity(0, lastUpdated, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.deleteBook(bookId);
        return Response.createResponseEntity(0, "Book was deleted successfully", token, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "genres", method = RequestMethod.GET)
    public List<String> getGenres() {
        return Stream.of(Genre.values())
                .map(Genre::name)
                .collect(Collectors.toList());
    }

    /* ----------------------------------------exception handlers*------------------------------------------ */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, e.getMessage().isEmpty() ? "Forbidden" : e.getMessage(), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> bookNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, e.getMessage().isEmpty() ? "Book is not found" : e.getMessage(), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IsNotPremiumUserException.class)
    public ResponseEntity<?> isNotPremiumUser(IsNotPremiumUserException e) {
        return Response.createResponseEntity(6, e.getMessage().isEmpty() ? "Only a premium user can do this" : e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TextConvertingException.class)
    public ResponseEntity<?> textConvertingError(TextConvertingException e) {
        return Response.createResponseEntity(7, "An error occurred while converting the book text. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return Response.createResponseEntity(8, "Problem with server's file system. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
