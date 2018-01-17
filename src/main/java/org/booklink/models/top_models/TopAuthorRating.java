package org.booklink.models.top_models;

/**
 * Created by mhenr on 02.12.2017.
 */
public class TopAuthorRating {
    private String username;
    private String firstName;
    private String lastName;
    private long totalEstimation;
    private long totalVotes;
    private Boolean premium;

    public TopAuthorRating(final String username, final String firstName, final String lastName,
                           final long totalEstimation, final long totalVotes, final Boolean premium) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalEstimation = totalEstimation;
        this.totalVotes = totalVotes;
        this.premium = premium;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getTotalEstimation() {
        return totalEstimation;
    }

    public void setTotalEstimation(long totalEstimation) {
        this.totalEstimation = totalEstimation;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}
