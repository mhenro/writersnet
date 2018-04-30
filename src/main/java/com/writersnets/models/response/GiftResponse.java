package com.writersnets.models.response;

import com.writersnets.models.GiftType;
import com.writersnets.models.entities.Gift;

/**
 * Created by mhenr on 27.01.2018.
 */
public class GiftResponse {
    private Long id;
    private GiftType giftType;
    private Long cost;
    private String name;
    private String description;
    private String image;

    public GiftResponse(final Gift gift) {
        if (gift == null) {
            return;
        }
        this.id = gift.getId();
        this.giftType = gift.getGiftType();
        this.cost = gift.getCost();
        this.name = gift.getName();
        this.description = gift.getDescription();
        this.image = gift.getImage();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
