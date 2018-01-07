package org.booklink.models.response;


import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class FriendshipResponse {
    private Date date;
    private Boolean active;
    private String id;
    private String name;
    private String section;
    private String avatar;

    public FriendshipResponse(final Date date, final Boolean active, final String id, final String firstName, final String lastName,
                              final String section, final String avatar) {
        this.date = date;
        this.active = active;
        this.id = id;
        this.name = firstName + " " + lastName;
        this.section = section;
        this.avatar = avatar;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
