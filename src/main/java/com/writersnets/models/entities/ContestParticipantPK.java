package com.writersnets.models.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 25.02.2018.
 */
@Embeddable
public class ContestParticipantPK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;


    public Contest getContest() {
        return contest;
    }
    public void setContest(Contest contest) {
        this.contest = contest;
    }
    public User getParticipant() {
        return participant;
    }
    public void setParticipant(User participant) {
        this.participant = participant;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContestParticipantPK that = (ContestParticipantPK) o;

        if (getContest() != null ? !getContest().equals(that.getContest()) : that.getContest() != null) return false;
        if (getParticipant() != null ? !getParticipant().equals(that.getParticipant()) : that.getParticipant() != null)
            return false;
        return getBook() != null ? getBook().equals(that.getBook()) : that.getBook() == null;

    }

    @Override
    public int hashCode() {
        int result = getContest() != null ? getContest().hashCode() : 0;
        result = 31 * result + (getParticipant() != null ? getParticipant().hashCode() : 0);
        result = 31 * result + (getBook() != null ? getBook().hashCode() : 0);
        return result;
    }
}
