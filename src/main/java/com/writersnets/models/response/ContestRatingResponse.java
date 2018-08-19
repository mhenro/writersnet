package com.writersnets.models.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContestRatingResponse {
    private Long bookId;
    private String name;
    private Double rating;

    public ContestRatingResponse(final Long bookId, final String bookName, final Double rating) {
        this.bookId = bookId;
        this.name = bookName;
        this.rating = rating;
    }
}
