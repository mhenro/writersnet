package org.booklink.services;

import org.booklink.models.entities.Book;
import org.booklink.models.entities.News;
import org.booklink.models.entities.User;
import org.booklink.models.response.NewsResponse;
import org.booklink.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by mhenr on 13.12.2017.
 */
@Service
@Transactional(readOnly = true)
public class NewsService {
    private NewsRepository newsRepository;

    public enum NEWS_TYPE {
        BOOK_CREATED(0),
        BOOK_UPDATED(1),
        BOOK_DELETED(2),
        COMMENT(3),
        PERSONAL_INFO_UPDATED(4),
        SERIE_CREATED(5),
        SERIE_UPDATED(6),
        SERIE_DELETED(7),
        FRIEND_ADDED(8),
        FRIEND_REMOVED(9);
        private final long type;

        NEWS_TYPE(final long type) {
            this.type = type;
        }

        public long getType() {
            return type;
        }
    }

    @Autowired
    public NewsService(final NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author, final Book book) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setBook(book);
        news.setCreated(new Date());
        newsRepository.save(news);
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author, final User subscription) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setSubscription(subscription);
        news.setCreated(new Date());
        newsRepository.save(news);
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setCreated(new Date());
        newsRepository.save(news);
    }

    public Page<NewsResponse> getNews(final Pageable pageable) {
        return newsRepository.findAllNews(getCurrentUser(), pageable);
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        return currentUser;
    }
}
