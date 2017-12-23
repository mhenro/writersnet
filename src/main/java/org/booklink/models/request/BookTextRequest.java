package org.booklink.models.request;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mhenr on 31.10.2017.
 */
public class BookTextRequest {
    private Long bookId;
    private String userId;
    private MultipartFile text;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MultipartFile getText() {
        return text;
    }

    public void setText(MultipartFile text) {
        this.text = text;
    }
}
