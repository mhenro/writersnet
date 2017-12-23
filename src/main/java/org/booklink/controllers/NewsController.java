package org.booklink.controllers;

import org.booklink.models.response.NewsResponse;
import org.booklink.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mhenr on 13.12.2017.
 */
@RestController
public class NewsController {
    private NewsService newsService;

    @Autowired
    public NewsController(final NewsService newsService) {
        this.newsService = newsService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @CrossOrigin
    @RequestMapping(value = "news", method = RequestMethod.GET)
    public Page<NewsResponse> getNews(final Pageable pageable) {
        return newsService.getNews(pageable);
    }
}
