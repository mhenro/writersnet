package org.booklink.controllers;

import org.booklink.models.entities.Book;
import org.booklink.models.request_models.BookRequest;
import org.booklink.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mhenr on 30.11.2017.
 */
@RestController
public class BookTopController {
    private BookService bookService;

    @Autowired
    public BookTopController(final BookService bookService) {
        this.bookService = bookService;
    }

    @CrossOrigin
    @RequestMapping(value = "top/books/novelties", method = RequestMethod.GET)
    public Page<Book> getTopNovelties(Pageable pageable) {
        return bookService.getBooksByLastUpdate(pageable);
    }

    /* TODO: fixme
    @CrossOrigin
    @RequestMapping(value = "top/books/volume", method = RequestMethod.GET)
    public Page<BookRequest> getTopVolume(Pageable pageable) {
        return bookService.getBooksBySize(pageable);
    }*/

    @CrossOrigin
    @RequestMapping(value = "top/books/rating", method = RequestMethod.GET)
    public Page<BookRequest> getTopRating(Pageable pageable) {
        return bookService.getBooksByRating(pageable);
    }
}
