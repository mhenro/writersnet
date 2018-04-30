package com.writersnets.models.response;

import com.writersnets.models.entities.BookSerie;

/**
 * Created by mhenr on 20.12.2017.
 */
public class BookSerieResponse {
    private Long id;
    private String name;

    public BookSerieResponse(){}

    public BookSerieResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

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
}
