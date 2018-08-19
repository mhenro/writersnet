package com.writersnets.models.entities.groups;

import com.writersnets.models.entities.AbstractEntity;
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
@Table(name = "subscribers")
@Audited
@Getter @Setter @NoArgsConstructor
public class Subscriber extends AbstractEntity {
    @EmbeddedId
    private SubscriberPK subscriberPK;
    private LocalDateTime added;
}
