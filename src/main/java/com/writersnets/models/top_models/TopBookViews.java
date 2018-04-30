package com.writersnets.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopBookViews {
    private long id;
    private String name;
    private long views;

    public TopBookViews(final long id, final String name, final long views) {
        this.id = id;
        this.name = name;
        this.views = views;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
