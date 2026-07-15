package models;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private boolean isAvailable;

    public Book(String isbn, String title, String author, String genre, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isAvailable = true;
    }

    public Book(String isbn, String title, String author, String genre, int publicationYear, boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isAvailable = isAvailable;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getPublicationYear() { return publicationYear; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setGenre(String genre) { this.genre = genre; }

    public void borrowBook() {
        this.isAvailable = false;
    }

    public void returnBook() {
        this.isAvailable = true;
    }

    public String getBookInfo() {
        return "ISBN: " + this.isbn + 
               "\nTitle: " + this.title + 
               "\nAuthor: " + this.author + 
               "\nGenre: " + this.genre + 
               "\nYear: " + this.publicationYear + 
               "\nAvailable: " + (this.isAvailable ? "Yes" : "No");
    }
}
