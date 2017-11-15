package org.booklink.controllers;

import org.booklink.models.request_models.BookComment;
import org.booklink.models.Genre;
import org.booklink.models.Response;
import org.booklink.models.entities.*;
import org.booklink.models.exceptions.ObjectAlreadyExistException;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.models.request_models.Serie;
import org.booklink.repositories.*;
import org.booklink.services.BookService;
import org.booklink.services.CommentsService;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mhenr on 02.10.2017.
 */
@RestController
public class BookController {
    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.GET)
    public Page<Book> getBooks(Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable Long bookId) {
        Book book = bookService.getBook(bookId);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Book not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.POST)
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        Response<String> response = new Response<>();
        Long bookId = null;
        try {
            bookId = bookService.saveBook(book);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage(String.valueOf(bookId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        Response<String> response = new Response<>();
        try {
            bookService.deleteBook(bookId);
        } catch (Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Book was deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "genres", method = RequestMethod.GET)
    public List<String> getGenres() {
        return Stream.of(Genre.values())
                .map(Genre::name)
                .collect(Collectors.toList());
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> unauthorizedUser(UnauthorizedUserException e) {
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Forbidden");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> bookNotFound(ObjectNotFoundException e) {
        Response<String> response = new Response<>();
        response.setCode(5);
        response.setMessage("Book not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
