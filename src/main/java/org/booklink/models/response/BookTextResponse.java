package org.booklink.models.response;

/**
 * Created by mhenr on 24.12.2017.
 */
public class BookTextResponse {
    private Long id;
    private String text;

    public BookTextResponse(final Long id, final String text) {
        this.id = id;
        this.text = text;
    }

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
