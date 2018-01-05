package org.booklink.models.response;

/**
 * Created by mhenr on 05.01.2018.
 */
public class ReviewResponse {
    private Long id;
    private Long bookId;
    private String bookName;
    private String bookCover;
    private String name;
    private String authorId;
    private String authorFullName;
    private Integer score;
    private String text;

    public ReviewResponse(final Long id, final Long bookId, final String bookName, final String bookCover, final String name,
                          final String authorId, final String firstName, final String lastName, final Integer score, final String text) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.name = name;
        this.authorId = authorId;
        this.authorFullName = firstName + " " + lastName;
        this.score = score;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
