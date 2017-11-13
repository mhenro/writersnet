package org.booklink.models.request_models;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mhenr on 27.10.2017.
 */
public class AvatarRequest {
    private String userId;
    private MultipartFile avatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
