package com.writersnets.models.response;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.contests.Contest;
import com.writersnets.models.entities.users.News;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by mhenr on 13.12.2017.
 */
@NoArgsConstructor
@Getter
@Setter
public class NewsResponse {
    private Long id;
    private Long type;
    private String authorId;
    private String authorFullName;
    private String authorAvatar;
    private Long bookId;
    private String bookName;
    private LocalDateTime created;
    private String subscriptionId;
    private String subscriptionFullName;
    private Long contestId;
    private String contestName;

    public NewsResponse(final News news) {
        if (news == null) {
            return;
        }
        this.id = news.getId();
        this.type = news.getType();
        this.authorId = news.getAuthor().getUsername();
        this.authorFullName = news.getAuthor().getFullName();
        this.authorAvatar = news.getAuthor().getAvatar();
        this.bookId = Optional.ofNullable(news.getBook()).map(Book::getId).orElse(null);
        this.bookName = Optional.ofNullable(news.getBook()).map(Book::getName).orElse(null);
        this.created = news.getCreated();
        this.subscriptionId = Optional.ofNullable(news.getSubscription()).map(User::getUsername).orElse(null);
        this.subscriptionFullName = Optional.ofNullable(news.getSubscription()).map(User::getFullName).orElse(null);
        this.contestId = Optional.ofNullable(news.getContest()).map(Contest::getId).orElse(null);
        this.contestName = Optional.ofNullable(news.getContest()).map(Contest::getName).orElse(null);
    }
}
