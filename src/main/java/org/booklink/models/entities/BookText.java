package org.booklink.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by mhenr on 31.10.2017.
 */
@Entity
@Table(name = "book_text")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BookText {
    private Long id;
    private String text;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
