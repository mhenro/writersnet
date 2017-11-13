package org.booklink.models.request_models;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mhenr on 27.10.2017.
 */
public class CoverRequest {
    private Long id;
    private String userId;
    private MultipartFile cover;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MultipartFile getCover() {
        return cover;
    }

    public void setCover(MultipartFile cover) {
        this.cover = cover;
    }
}
