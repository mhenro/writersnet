package org.booklink.models.response;

/**
 * Created by mhenr on 10.02.2018.
 */
public class BookCostResponse {
    private Long cost;
    private String name;
    private String description;

    public BookCostResponse(final Long cost, final String name, final String description) {
        this.cost = cost;
        this.name = name;
        this.description = description;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
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
}
