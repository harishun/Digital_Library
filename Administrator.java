import java.util.ArrayList;

public class Administrator extends User {
    static private int adminNo = 1;
    
    Administrator(String name, String email, String phoneNumber, String password){
        super("ADM-" + adminNo++, name, email, phoneNumber, password, "Administrator");
    }
    
    public static void setAdminNo(int nextNo) {
        adminNo = nextNo;
    }

    public void viewInventory() {
        for(Book b : DigitalLibrary.books){
            System.out.println("\n\n" + b.getBookInfo());
        }
        System.out.println("\n\n");
    }

    public void viewUsers() {
        for(User u: DigitalLibrary.users){
            System.out.println("\n\n" + u.getUserInfo());
        }
        System.out.println("\n\n");
    }

    public void addBook(ArrayList<Book> booksList) {
        System.out.print("Enter book title: ");
        String title = DigitalLibrary.scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = DigitalLibrary.scanner.nextLine();
        System.out.print("Enter book ISBN: ");
        String isbn = DigitalLibrary.scanner.nextLine();
        Integer year = null;
        System.out.print("Enter publication year: ");
        int yearInput = DigitalLibrary.scanner.nextInt();
        DigitalLibrary.scanner.nextLine();
        if (yearInput > 0) {
            year = yearInput;
        }
        String genre = null;
        System.out.print("Enter book genre (optional, press Enter to skip): ");
        String genreInput = DigitalLibrary.scanner.nextLine();
        if (!genreInput.isEmpty()) {
            genre = genreInput;
        }
        Book newBook = new Book(isbn, title, author, year, genre);
        booksList.add(newBook);
        System.out.println("Book added successfully.");
    }

    public void editBook(ArrayList<Book> booksList) {
        String isbnInput = null;
        String genreInput = null;
        boolean bookFound = false;
        System.out.println("Change the Genre of a Book");
        System.out.print("Please Enter the ISBN of the Book : ");
        isbnInput = DigitalLibrary.scanner.nextLine();
        for(Book b : booksList){
            if(b.getISBN().equals(isbnInput)){
                bookFound = true;
                System.out.println("Enter the new genre of the Book");
                genreInput = DigitalLibrary.scanner.nextLine();
                b.setGenre(genreInput);
                System.out.println("Book genre updated successfully.");
                break;
            }
        }
        if(!bookFound){
            System.out.println("The Book you entered doesn't exist.");
        }
    }

    public void deleteBook(ArrayList<Book> booksList) {
        String isbnInput = null;
        boolean bookFound = false;
        System.out.println("Remove a Book from inventory");
        System.out.print("Please Enter the ISBN of the Book : ");
        isbnInput = DigitalLibrary.scanner.nextLine();
        for(Book b : booksList){
            if(b.getISBN().equals(isbnInput)){
                bookFound = true;
                booksList.remove(b);
                System.out.println("Book Succesfully Removed.");
                break;
            }
        }
        if(!bookFound){
            System.out.println("The Book you entered doesn't exist.");
        }
    }

    public void viewTransaction() {
        System.out.println("\n--- All System Transactions ---");
        if (DigitalLibrary.transactions.isEmpty()) {
            System.out.println("No transactions have been recorded yet.");
        } else {
            for (Transaction t : DigitalLibrary.transactions) {
                System.out.println("\n" + t.getTransaction());
                System.out.println("----------------------------");
            }
        }
        System.out.println("-------------------------------\n");
    }
}
