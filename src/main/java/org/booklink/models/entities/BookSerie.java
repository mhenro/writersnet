package org.booklink.models.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by mhenr on 22.10.2017.
 */
@Entity
public class BookSerie implements Serializable {
    private Long id;
    private String name;
    private String userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
