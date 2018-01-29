package org.booklink.models.top_models;

import java.time.LocalDateTime;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopBookNovelties {
    private long id;
    private String name;
    private LocalDateTime lastUpdate;

    public TopBookNovelties(final long id, final String name, final LocalDateTime lastUpdate) {
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

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
