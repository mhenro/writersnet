package com.writersnets.models.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by mhenr on 10.02.2018.
 */
@Entity
@Table(name = "user_gift")
public class UserGift extends AbstractEntity {
    @GenericGenerator(
            name = "user_gift_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_gift_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "0"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gift_generator")
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_id", nullable = false)
    private Gift gift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private User sender;

    @Column(name = "message")
    private String sendMessage;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Gift getGift() {
        return gift;
    }
    public void setGift(Gift gift) {
        this.gift = gift;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public String getSendMessage() {
        return sendMessage;
    }
    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }
}
