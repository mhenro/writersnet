package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 13.12.2017.
 */
@Entity
@Audited
@Getter @Setter @NoArgsConstructor
public class News extends AbstractIdEntity {
    @Column(name = "news_type")
    private Long type;  /*
                            0 - book was created
                            1 - book was updated
                            2 - book was deleted
                            3 - leave a comment to book
                            4 - author personal info was updated
                            5 - new serie was added
                            6 - serie was updated
                            7 - serie was deleted
                            8 - friend was added
                            9 - friend was removed
                            10 - contest was created
                        */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private User subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;
}
