package com.writersnets.models.entities;

import com.writersnets.models.GiftType;

import javax.persistence.*;

/**
 * Created by mhenr on 27.01.2018.
 */
@Entity
public class Gift extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gift_type", nullable = false)
    private GiftType giftType;
    private Long cost;
    private String name;
    private String description;
    private String image;
    private String category;


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
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}