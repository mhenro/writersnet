package com.writersnets.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopBookVolume {
    private long id;
    private String name;
    private int volume;

    public TopBookVolume(final long id, final String name, final int volume) {
        this.id = id;
        this.name = name;
        this.volume = volume;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
