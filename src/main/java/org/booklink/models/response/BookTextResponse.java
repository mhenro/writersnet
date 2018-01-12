package org.booklink.models.response;

/**
 * Created by mhenr on 24.12.2017.
 */
public class BookTextResponse {
    private Long id;
    private String text;
    private String prevText;

    public BookTextResponse(final Long id, final String text, final String prevText) {
        this.id = id;
        this.text = text;
        this.prevText = prevText;
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

    public String getPrevText() {
        return prevText;
    }

    public void setPrevText(String prevText) {
        this.prevText = prevText;
    }
}
