package com.writersnets.models.entities.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.books.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mhenr on 31.10.2017.
 */
@Entity
@Table(name = "texts")
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter @Setter @NoArgsConstructor
public class BookText extends AbstractIdEntity {
    private String text;

    @Column(name = "prev_text")
    private String prevText;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
