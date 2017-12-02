package org.booklink.models.top_models;

import java.util.Date;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopBookNovelties {
    private long id;
    private String name;
    private Date lastUpdate;

    public TopBookNovelties(final long id, final String name, final Date lastUpdate) {
        this.id = id;
        this.name = name;
        this.lastUpdate = lastUpdate;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
