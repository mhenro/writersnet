package org.booklink.models.request;

/**
 * Created by mhenr on 22.10.2017.
 */
public class TotalSize {
    private long totalSize;  //bytes
    private long totalBooks;

    public TotalSize() {
        totalSize = 0;
        totalBooks = 0;
    }

    public TotalSize(long totalSize, long totalBooks) {
        this.totalSize = totalSize;
        this.totalBooks = totalBooks;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }
}
