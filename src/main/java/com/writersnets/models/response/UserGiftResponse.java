package com.writersnets.models.response;

import com.writersnets.models.entities.users.Gift;

/**
 * Created by mhenr on 17.02.2018.
 */
public class UserGiftResponse {
    private Long id;
    private GiftResponse gift;
    private String userId;
    private String senderId;
    private String senderFullName;
    private String sendMessage;

    public UserGiftResponse(final Long id, final Gift gift, final String userId, final String senderId,
                            final String senderFirstName, final String senderLastName, final String sendMessage) {
        this.id = id;
        this.gift = new GiftResponse(gift);
        this.userId = userId;
        this.senderId = senderId;
        this.senderFullName = senderFirstName + " " + senderLastName;
        this.sendMessage = sendMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiftResponse getGift() {
        return gift;
    }

    public void setGift(GiftResponse gift) {
        this.gift = gift;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }
}
