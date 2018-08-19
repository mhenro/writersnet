package com.writersnets.models.entities.users;

import com.writersnets.models.entities.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 03.02.2018.
 */
@Entity
@Table(name = "user_book")
@Audited
@Getter @Setter @NoArgsConstructor
public class UserBook extends AbstractEntity {
    @EmbeddedId
    private UserBookPK userBookPK;
}
