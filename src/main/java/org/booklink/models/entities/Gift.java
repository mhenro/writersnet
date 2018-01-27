package org.booklink.models.entities;

import org.booklink.models.GiftType;

import javax.persistence.*;

/**
 * Created by mhenr on 27.01.2018.
 */
@Entity
public class Gift {
    private Long id;
    private GiftType giftType;
    private Long cost;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gift_type", nullable = false)
    public GiftType getGiftType() {
        return giftType;
    }

    public void setGiftType(GiftType giftType) {
        this.giftType = giftType;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
