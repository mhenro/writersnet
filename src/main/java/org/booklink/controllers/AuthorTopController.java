package org.booklink.controllers;

import org.booklink.models.top_models.TopAuthorRating;
import org.booklink.services.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mhenr on 02.12.2017.
 */
@RestController
public class AuthorTopController {
    private AuthorService authorService;

    @Autowired
    public AuthorTopController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @CrossOrigin
    @RequestMapping(value = "top/authors/rating", method = RequestMethod.GET)
    public Page<TopAuthorRating> getTopRating(Pageable pageable) {
        return authorService.getAuthorsByRating(pageable);
    }
}
