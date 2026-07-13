public class Book {
    final private String ISBN;
    final private String title;
    final private String author;
    private String genre;
    final private Integer publicationYear;
    private boolean isAvailable;

    Book(String ISBN, String title, String author, int publicationYear, String genre){
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
    public String getBookInfo() {
        return "ISBN: " + this.ISBN + "\nTitle: " + this.title + "\nAuthor: " + this.author + "\nGenre: " + this.genre + "\nYear: " + this.publicationYear + "\nAvailable: " + this.isAvailable;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public void borrowed(){
        this.isAvailable = false;
    }

    public void returned(){
        this.isAvailable = true;
    }
}
