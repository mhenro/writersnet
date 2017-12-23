package org.booklink.models.request;

/**
 * Created by mhenr on 08.12.2017.
 */
public class MessageRequest {
    private String creator;
    private String primaryRecipient;
    private String text;
    private Long groupId;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPrimaryRecipient() {
        return primaryRecipient;
    }

    public void setPrimaryRecipient(String primaryRecipient) {
        this.primaryRecipient = primaryRecipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
