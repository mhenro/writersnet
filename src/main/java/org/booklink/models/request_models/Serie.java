package org.booklink.models.request_models;

/**
 * Created by mhenr on 13.11.2017.
 */
public class Serie {
    private Long id;
    private String name;
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
