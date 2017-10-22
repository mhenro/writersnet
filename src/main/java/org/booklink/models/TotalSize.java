package org.booklink.models;

/**
 * Created by mhenr on 22.10.2017.
 */
public class TotalSize {
    private int totalSize;  //bytes
    private int totalBooks;

    public TotalSize() {
        totalSize = 0;
        totalBooks = 0;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }
}
