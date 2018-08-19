package com.writersnets.models.entities.contests;

import com.writersnets.models.entities.books.Book;
import com.writersnets.models.entities.users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by mhenr on 25.02.2018.
 */
@Embeddable
@Audited
@Getter @Setter @NoArgsConstructor
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
