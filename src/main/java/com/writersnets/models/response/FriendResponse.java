package com.writersnets.models.response;

/**
 * Created by mhenr on 08.12.2017.
 */
public class FriendResponse {
    private String friendId;
    private String fullName;

    public FriendResponse(final String friendId, final String firstName, final String lastName) {
        this.friendId = friendId;
        this.fullName = firstName + " " + lastName;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
