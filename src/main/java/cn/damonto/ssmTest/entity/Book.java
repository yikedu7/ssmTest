package cn.damonto.ssmTest.entity;

public class Book {

    private long bookId;
    private String bookName;
    private int bookNumber;

    public Book(){}

    public Book (long bookId, String bookName, int bookNumber) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookNumber = bookNumber;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public long getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    @Override
    public String toString() {
        return "Book [book id = " + bookId + ", book name = " + bookName + ", books number = " + bookNumber + "]";
    }
}
