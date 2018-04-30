package com.writersnets.controllers;

import com.writersnets.models.top_models.*;
import com.writersnets.services.BookService;
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
@CrossOrigin
public class BookTopController {
    private BookService bookService;

    @Autowired
    public BookTopController(final BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "top/books/novelties", method = RequestMethod.GET)
    public Page<TopBookNovelties> getTopNovelties(Pageable pageable) {
        return bookService.getBooksByLastUpdate(pageable);
    }

    @RequestMapping(value = "top/books/rating", method = RequestMethod.GET)
    public Page<TopBookRating> getTopRating(Pageable pageable) {
        return bookService.getBooksByRating(pageable);
    }

    @RequestMapping(value = "top/books/volume", method = RequestMethod.GET)
    public Page<TopBookVolume> getTopVolume(Pageable pageable) {
        return bookService.getBooksByVolume(pageable);
    }

    @RequestMapping(value = "top/books/comments", method = RequestMethod.GET)
    public Page<TopBookComments> getTopComments(Pageable pageable) {
        return bookService.getBooksByComments(pageable);
    }

    @RequestMapping(value = "top/books/views", method = RequestMethod.GET)
    public Page<TopBookViews> getTopViews(Pageable pageable) {
        return bookService.getBooksByViews(pageable);
    }
}
