package com.writersnets.services.authors;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.users.News;
import com.writersnets.models.entities.users.User;
import com.writersnets.models.response.NewsResponse;
import com.writersnets.repositories.NewsRepository;
import com.writersnets.services.security.AuthorizedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 13.12.2017.
 */
@Service
@Transactional(readOnly = true)
public class NewsService {
    private NewsRepository newsRepository;
    private AuthorizedUserService authorizedUserService;

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
        FRIEND_REMOVED(9),
        CONTEST_CREATED(10),
        JOIN_IN_CONTEST_AS_JUDGE(11),
        JOIN_IN_CONTEST_AS_PARTICIPANT(12),
        WON_IN_CONTEST(13);

        private final long type;

        NEWS_TYPE(final long type) {
            this.type = type;
        }

        public long getType() {
            return type;
        }
    }

    @Autowired
    public NewsService(final NewsRepository newsRepository, final AuthorizedUserService authorizedUserService) {
        this.newsRepository = newsRepository;
        this.authorizedUserService = authorizedUserService;
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author, final Book book) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setBook(book);
        news.setCreated(LocalDateTime.now());
        newsRepository.save(news);
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author, final User subscription) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setSubscription(subscription);
        news.setCreated(LocalDateTime.now());
        newsRepository.save(news);
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author, final Contest contest, final Book book) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setContest(contest);
        news.setBook(book);
        news.setCreated(LocalDateTime.now());
        newsRepository.save(news);
    }

    @Transactional
    public void createNews(final NEWS_TYPE type, final User author) {
        final News news = new News();
        news.setType(type.getType());
        news.setAuthor(author);
        news.setCreated(LocalDateTime.now());
        newsRepository.save(news);
    }

    public Page<NewsResponse> getNews(final Pageable pageable) {
        return newsRepository.findAllNews(authorizedUserService.getAuthorizedUser().getUsername(), pageable);
    }
}
