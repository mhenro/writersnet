package com.writersnets.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "friends")
@Audited
@Getter @Setter @NoArgsConstructor
public class Friend extends AbstractEntity {
    @EmbeddedId
    private FriendPK friendPK;
    private LocalDateTime added;
}
