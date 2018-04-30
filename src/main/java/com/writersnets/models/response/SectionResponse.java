package com.writersnets.models.response;

import com.writersnets.models.entities.Section;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by mhenr on 20.12.2017.
 */
public class SectionResponse {
    private long id;
    private String name;
    private String description;
    private LocalDate lastUpdated;

    public SectionResponse(){}

    public SectionResponse(final Section section) {
        if (section == null) {
            return;
        }
        this.id = section.getId();
        this.name = section.getName();
        this.description = section.getDescription();
        this.lastUpdated = section.getLastUpdated();
    }

    public SectionResponse(final Long id, final String name, final String description, final LocalDate lastUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.lastUpdated = lastUpdated;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
