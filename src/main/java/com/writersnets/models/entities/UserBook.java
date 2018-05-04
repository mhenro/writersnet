package com.writersnets.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by mhenr on 03.02.2018.
 */
@Entity
@Table(name = "user_book")
public class UserBook extends AbstractEntity {
    @EmbeddedId
    private UserBookPK userBookPK;


    public UserBookPK getUserBookPK() {
        return userBookPK;
    }
    public void setUserBookPK(UserBookPK userBookPK) {
        this.userBookPK = userBookPK;
    }
}
