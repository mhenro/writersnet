package com.writersnets.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by mhenr on 30.12.2017.
 */
@Entity
@Table(name = "subscribers")
public class Subscriber extends AbstractEntity {
    @EmbeddedId
    private SubscriberPK subscriberPK;
    private LocalDateTime added;


    public SubscriberPK getSubscriberPK() {
        return subscriberPK;
    }
    public void setSubscriberPK(SubscriberPK subscriberPK) {
        this.subscriberPK = subscriberPK;
    }
    public LocalDateTime getAdded() {
        return added;
    }
    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
