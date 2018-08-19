package com.writersnets.models.entities.books;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 06.11.2017.
 */
@Entity
@Table(name = "comments")
@Audited
@Getter @Setter @NoArgsConstructor
public class Comment extends AbstractIdEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "related_to")
    private Comment relatedTo;

    private LocalDateTime created;
}
