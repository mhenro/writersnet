package org.booklink.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.booklink.models.Genre;
import org.booklink.models.Response;
import org.booklink.models.entities.*;
import org.booklink.models.exceptions.ObjectNotFoundException;
import org.booklink.models.exceptions.UnauthorizedUserException;
import org.booklink.repositories.AuthorRepository;
import org.booklink.repositories.BookRepository;
import org.booklink.repositories.RatingRepository;
import org.booklink.repositories.SerieRepository;
import org.booklink.utils.ObjectHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private SerieRepository serieRepository;
    private RatingRepository ratingRepository;

    @Autowired
    public BookController(AuthorRepository authorRepository, BookRepository bookRepository, SerieRepository serieRepository, RatingRepository ratingRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.serieRepository = serieRepository;
        this.ratingRepository = ratingRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "series", method = RequestMethod.GET)
    public Page<BookSerie> getBookSeries(Pageable pageable) {
        Page<BookSerie> series = serieRepository.findAll(pageable);
        return series;
    }

    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.GET)
    public Page<Book> getBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        books.forEach(book -> {
            hideAuthInfo(book);
            calcBookSize(book);
            hideText(book);
            removeRecursionFromBook(book);
        });
        return books;
    }

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<?> getBook(@PathVariable Long bookId) {
        Book book = bookRepository.findOne(bookId);
        if (book != null) {
            hideAuthInfo(book);
            calcBookSize(book);
            //hideText(book);
            removeRecursionFromBook(book);
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        Response<String> response = new Response<>();
        response.setCode(1);
        response.setMessage("Book not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private void hideAuthInfo(Book book) {
        User user = book.getAuthor();
        user.setPassword("");
        user.setActivationToken("");
        user.setAuthority("");
    }

    private void removeRecursionFromBook(Book book) {
        User user = book.getAuthor();
        user.setBooks(null);
        user.setSection(null);
    }

    private void calcBookSize(Book book) {
        int size = Optional.ofNullable(book.getBookText()).map(bookText -> bookText.getText().length()).orElse(0);
        book.setSize(size);
    }

    private void hideText(Book book) {
        book.setBookText(null);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books", method = RequestMethod.POST)
    public ResponseEntity<?> saveBook(@RequestBody Book book) {
        Response<String> response = new Response<>();
        Book savedBook;
        try {
            if (book.getId() == null) { //new book
                savedBook = new Book();
                savedBook.setCreated(new Date());
            } else {    //saved book was edited
                savedBook = bookRepository.findOne(book.getId());
                if (savedBook == null) {
                    throw new ObjectNotFoundException();
                }
                checkCredentials(savedBook.getAuthor().getUsername());   //only owner can edit his book
                savedBook.setLastUpdate(new Date());
            }
            BeanUtils.copyProperties(book, savedBook, ObjectHelper.getNullPropertyNames(book));
            if (book.getSerieId() != null) {
                BookSerie bookSerie = serieRepository.findOne(book.getSerieId());
                if (bookSerie != null) {
                    savedBook.setBookSerie(bookSerie);
                }
            } else {
                savedBook.setBookSerie(null);
            }
            if (book.getAuthorName() != null) {
                User user = authorRepository.findOne(book.getAuthorName());
                if (user != null) {
                    savedBook.setAuthor(user);
                }
            }/*
            if (book.getText() == null) {
                savedBook.setText("");
            }*/
            bookRepository.save(savedBook);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage(savedBook.getId().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "books/{bookId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        Response<String> response = new Response<>();
        try {
            Book book = bookRepository.findOne(bookId);
            if (book == null) {
                throw new ObjectNotFoundException();
            }
            checkCredentials(book.getAuthor().getUsername());   //only owner can delete his book
            bookRepository.delete(bookId);
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

    @CrossOrigin
    @RequestMapping(value = "books/{bookId}/rating/{value}", method = RequestMethod.GET)
    public ResponseEntity<?> addStar(@PathVariable Long bookId, @PathVariable Integer value, HttpServletRequest request) {

        //TODO: save client IP via request.getRemoteAddr() to rating table. Create new embeddedId = bookId + estimation + IP

        Response<String> response = new Response<>();
        try {
            RatingId ratingId = new RatingId();
            ratingId.setBookId(bookId);
            ratingId.setEstimation(value);
            Rating rating = ratingRepository.findOne(ratingId);
            if (rating != null) {
                rating.setUserCount(rating.getUserCount() + 1);
            } else {
                rating = new Rating();
                rating.setRatingId(ratingId);
                rating.setUserCount(1);
            }
            ratingRepository.save(rating);
        } catch(Exception e) {
            response.setCode(1);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.setCode(0);
        response.setMessage("Your vote was added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void checkCredentials(final String login) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        if (!currentUser.equals(login)) {
            throw new UnauthorizedUserException();
        }
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
