package org.booklink.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopBookComments {
    private long id;
    private String name;
    private long count;

    public TopBookComments(final long id, final String name, final long count) {
        this.id = id;
        this.name = name;
        this.count = count;
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
