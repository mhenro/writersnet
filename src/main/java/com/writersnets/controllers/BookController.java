package com.writersnets.controllers;

import com.writersnets.models.Genre;
import com.writersnets.models.Response;
import com.writersnets.models.exceptions.*;
import com.writersnets.models.request.BookRequest;
import com.writersnets.models.request.BookTextRequest;
import com.writersnets.models.request.CoverRequest;
import com.writersnets.models.response.BookCostResponse;
import com.writersnets.models.response.BookResponse;
import com.writersnets.models.response.BookWithTextResponse;
import com.writersnets.services.BookService;
import com.writersnets.services.SessionService;
import com.writersnets.utils.ControllerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.writersnets.utils.SecurityHelper.generateActivationToken;

/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
@CrossOrigin
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

    @RequestMapping(value = "books", method = RequestMethod.GET)
    public Page<BookResponse> getBooks(final String genre, final String language, final Pageable pageable) {
        return bookService.getBooks(genre, language, pageable);
    }

    @RequestMapping(value = "books/name/{bookName}", method = RequestMethod.GET)
    public Page<BookResponse> getBooksByName(@PathVariable final String bookName, final String genre, final String language, final Pageable pageable) {
        return bookService.getBooksByName(bookName, genre, language, pageable);
    }

    @RequestMapping(value = "count/books", method = RequestMethod.GET)
    public ResponseEntity<?> getBooksCount() {
        final long count = bookService.getBooksCount();
        return Response.createResponseEntity(0, count, null, HttpStatus.OK);
    }

    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable Long bookId, final Integer page, final Integer size) {
        BookWithTextResponse book = bookService.getBook(bookId, page, size);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "books/author/{authorId}", method = RequestMethod.GET)
    public Page<BookResponse> getBooksByAuthor(@PathVariable final String authorId, final Pageable pageable) {
        return bookService.getBooksByAuthor(authorId, pageable);
    }

    @RequestMapping(value = "books/cost/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBookCost(@PathVariable final Long bookId) {
        final BookCostResponse response = bookService.getBookCost(bookId);
        return Response.createResponseEntity(0, response, null, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "books/pdf/{bookId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getBookAsPdf(@PathVariable final Long bookId) throws IOException {
        final byte[] pdf = bookService.getBookAsPdf(bookId);
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.setContentLength(pdf.length);
        return new HttpEntity<>(pdf, header);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "books", method = RequestMethod.POST)
    public ResponseEntity<?> saveBook(@RequestBody final BookRequest book) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        Long bookId = bookService.saveBook(book);
        return Response.createResponseEntity(0, String.valueOf(bookId), token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "cover", method = RequestMethod.POST)
    public ResponseEntity<?> saveCover(final CoverRequest coverRequest) throws IOException {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.saveCover(coverRequest);
        return Response.createResponseEntity(0, "Cover was saved successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "cover/restore/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> restoreDefaultCover(@PathVariable final Long bookId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.restoreDefaultCover(bookId);
        return Response.createResponseEntity(0, "Cover was restored successfully", token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "text", method = RequestMethod.POST)
    public ResponseEntity<?> saveBookText(final BookTextRequest bookTextRequest) throws IOException {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        final LocalDateTime lastUpdated = bookService.saveBookText(bookTextRequest);
        return Response.createResponseEntity(0, lastUpdated, token, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        sessionService.updateSession(token);
        bookService.deleteBook(bookId);
        return Response.createResponseEntity(0, "Book was deleted successfully", token, HttpStatus.OK);
    }

    @RequestMapping(value = "genres", method = RequestMethod.GET)
    public List<String> getGenres() {
        return Stream.of(Genre.values())
                .map(Genre::name)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "books/paid/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> isUserHasBook(@PathVariable final Long bookId) {
        final String key = environment.getProperty("jwt.signing-key");
        String token = generateActivationToken(key);
        return Response.createResponseEntity(0, bookService.isUserHasBook(bookId), token, HttpStatus.OK);
    }

    /* ----------------------------------------exception handlers*------------------------------------------ */

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        return Response.createResponseEntity(1, ControllerHelper.getErrorOrDefaultMessage(e, "Forbidden"), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> bookNotFound(ObjectNotFoundException e) {
        return Response.createResponseEntity(5, ControllerHelper.getErrorOrDefaultMessage(e, "Book is not found"), null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IsNotPremiumUserException.class)
    public ResponseEntity<?> isNotPremiumUser(IsNotPremiumUserException e) {
        return Response.createResponseEntity(6, ControllerHelper.getErrorOrDefaultMessage(e, "Only a premium user can do this"), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TextConvertingException.class)
    public ResponseEntity<?> textConvertingError(TextConvertingException e) {
        return Response.createResponseEntity(7, "An error occurred while converting the book text. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException e) {
        return Response.createResponseEntity(8, "Problem with server's file system. Please try again later. Reason: " + e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> permissionDenied(PermissionDeniedException e) {
        return Response.createResponseEntity(9, ControllerHelper.getErrorOrDefaultMessage(e, "Permission denied"), null, HttpStatus.FORBIDDEN);
    }
}
