package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mhenr on 04.01.2018.
 */
@Entity
@Table(name = "reviews")
@Audited
@Getter @Setter @NoArgsConstructor
public class Review extends AbstractIdEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    private String name;
    private Integer score;
    private Long likes;
    private Long dislikes;
}
