package com.writersnets.controllers.authors;

import com.writersnets.models.top_models.TopAuthorBookCount;
import com.writersnets.models.top_models.TopAuthorComments;
import com.writersnets.models.top_models.TopAuthorRating;
import com.writersnets.models.top_models.TopAuthorViews;
import com.writersnets.services.authors.AuthorService;
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
@CrossOrigin
public class AuthorTopController {
    private AuthorService authorService;

    @Autowired
    public AuthorTopController(final AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping(value = "top/authors/rating", method = RequestMethod.GET)
    public Page<TopAuthorRating> getTopRating(Pageable pageable) {
        return authorService.getAuthorsByRating(pageable);
    }

    @RequestMapping(value = "top/authors/bookcount", method = RequestMethod.GET)
    public Page<TopAuthorBookCount> getTopBookCount(Pageable pageable) {
        return authorService.getAuthorsByBookCount(pageable);
    }

    @RequestMapping(value = "top/authors/comments", method = RequestMethod.GET)
    public Page<TopAuthorComments> getTopComments(Pageable pageable) {
        return authorService.getAuthorsByComments(pageable);
    }

    @RequestMapping(value = "top/authors/views", method = RequestMethod.GET)
    public Page<TopAuthorViews> getTopViews(Pageable pageable) {
        return authorService.getAuthorsByViews(pageable);
    }
}
