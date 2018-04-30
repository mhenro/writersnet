package com.writersnets.models.request;

/**
 * Created by mhenr on 11.12.2017.
 */
public class ReadMessageRequest {
    private String userId;
    private Long groupId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
