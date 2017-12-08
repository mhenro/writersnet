package org.booklink.models.request_models;

/**
 * Created by mhenr on 08.12.2017.
 */
public class MessageRequest {
    private String creator;
    private String text;
    private long groupId;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
