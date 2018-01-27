package org.booklink.models.response;

import org.booklink.models.GiftType;
import org.booklink.models.entities.Gift;

/**
 * Created by mhenr on 27.01.2018.
 */
public class GiftResponse {
    private Long id;
    private GiftType giftType;
    private Long cost;

    public GiftResponse(final Gift gift) {
        if (gift == null) {
            return;
        }
        this.id = gift.getId();
        this.giftType = gift.getGiftType();
        this.cost = gift.getCost();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
