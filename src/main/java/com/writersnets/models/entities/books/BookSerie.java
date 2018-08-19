package com.writersnets.models.entities.books;

import com.writersnets.models.entities.AbstractIdEntity;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Created by mhenr on 22.10.2017.
 */
@Entity
@Table(name = "series")
@Audited
@Getter @Setter @NoArgsConstructor
public class BookSerie extends AbstractIdEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
}
