public class Book {
    final private String ISBN;
    final private String title;
    final private String author;
    private String genre;
    private int publicationYear;
    private boolean isAvailable;
    Book(String ISBN, String title, String author, String genre){
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = -1; // Publication year is optional
        this.isAvailable = true; // By default, a new book is available
    }
    Book(String ISBN, String title, String author, String genre, int publicationYear){
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isAvailable = true; // By default, a new book is available
    }
    public String getISBN(){
        return this.ISBN;
    }
    public String getTitle(){
        return this.title;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getGenre(){
        return this.genre;
    }
    public int getPublicationYear(){
        return this.publicationYear;
    }
    public boolean isAvailable(){
        return this.isAvailable;
    }
    public void setAvailability(boolean availability){
        this.isAvailable = availability;
    }
}
