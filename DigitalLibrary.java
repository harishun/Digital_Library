import java.util.Scanner;
import java.util.ArrayList;

public class DigitalLibrary {
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Book> books = new ArrayList<>();
    static User currentUser = null;
    static boolean isLoggedIn = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        users.add(new Administrator("admin", "example@gmail.com", "0123456789", "admin"));
        users.add(new Member("John Doe", "example@gmail.com", "01233445453", "1234"));
        users.add(new Member("David Bosch", "example@gmail.com", "01233445453", "Pass"));

        books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018, "Education"));
        books.add(new Book("978-0201633610", "Design Patterns", "Erich Gamma", 1994, "Software Engineering"));
        books.add(new Book("978-0596007126", "Head First Design Patterns", "Eric Freeman", 2004, "Education"));
        books.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Software Engineering"));
        books.add(new Book("978-0201616224", "The Pragmatic Programmer", "Andrew Hunt", 1999, "Software Engineering"));
        
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Library System ---");
            System.out.println("0. Exit");
            System.out.println("1. Register as Member");
            System.out.println("2. Login");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerMember();
                    break;
                case 2:
                    login();
                    adminFunction();
                    memberFunctions();
                    break;
                case 0:
                    isLoggedIn = false;
                    System.out.println("Exiting.");
                    break;
            }
        }
        scanner.close();
    }

    public static void registerMember() {
        System.out.println("Registering as a new member.");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        Member newMember = new Member(name, email, phone, password);
        users.add(newMember);
        System.out.println("Success! Your ID: " + newMember.getUserId());
    }

    public static void login() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        boolean userFound = false;
        for (User u : users) {
            if (u.getUserId().equals(id)) {
                userFound = true;
                String[] authResult = u.authenticate(password);
                if (authResult != null) {
                    currentUser = u;
                    isLoggedIn = true;
                    System.out.println("Login successful! Role: " + authResult[1]);
                } else {
                    System.out.println("Incorrect password.");
                }
            }
        }
        if(!userFound){
            System.out.println("User does not exist.");
        }
    }
    
    public static void addBook() {
        if (isLoggedIn && currentUser.getRole().equals("Administrator")) {
            System.out.print("Enter book title: ");
            String title = scanner.nextLine();
            System.out.print("Enter book author: ");
            String author = scanner.nextLine();
            System.out.print("Enter book ISBN: ");
            String isbn = scanner.nextLine();
            Integer year = null;
            System.out.print("Enter publication year:");
            int yearInput = scanner.nextInt();
            scanner.nextLine();
            if (yearInput > 0) {
                year = yearInput;
            }
            String genre = null;
            System.out.print("Enter book genre (optional, press Enter to skip): ");
            String genreInput = scanner.nextLine();
            if (!genreInput.isEmpty()) {
                genre = genreInput;
            }
            Book newBook = new Book(isbn, title, author, year, genre);
            books.add(newBook);
            System.out.println("Book added successfully.");
        } else {
            System.out.println("You must be logged in as an administrator to add books.");
        }
        return;
    }

    public static void inventoryReport(){
        for(Book b : books){
            System.out.println("\n\n" + b.getBookInfo());
        }
        System.out.println("\n\n");
    }

    public static void userReport(){
        for(User u: users){
            System.out.println("\n\n" + u.getUserInfo());
        }
        System.out.println("\n\n");
    }

    public static void editBook(){
        String isbnInput = null;
        String genreInput = null;
        boolean bookFound = false;
        System.out.println("Change the Genre of a Book");
        System.out.print("Please Enter the ISBN of the Book : ");
        isbnInput = scanner.nextLine();
        for(Book b : books){
            if(b.getISBN().equals(isbnInput)){
                bookFound = true;
                System.out.println("Enter the new genre of the Book");
                genreInput = scanner.nextLine();
                b.setGenre(genreInput);
            }
        }
        if(!bookFound){
            System.out.println("The Book you entered doesn't exist.");
        }
    }

    public static void deleteBook(){
        String isbnInput = null;
        boolean bookFound = false;
        System.out.println("Remove a Book from inventory");
        System.out.print("Please Enter the ISBN of the Book : ");
        isbnInput = scanner.nextLine();
        for(Book b : books){
            if(b.getISBN().equals(isbnInput)){
                bookFound = true;
                books.remove(b);
                System.out.println("Book Succesfully Removed.");
            }
        }
        if(!bookFound){
            System.out.println("The Book you entered doesn't exist.");
        }
    }

    public static void adminFunction(){
        if(isLoggedIn && currentUser.getRole().equals("Administrator")){
            int adminChoice = -1;
            while(adminChoice != 0){
                System.out.println("\n---Admin Dashboard---");
                System.out.println("0. Logout");
                System.out.println("1. Generate Inventory Report");
                System.out.println("2. Generate User Report");
                System.out.println("3. Add New Book");
                System.out.println("4. Edit Book");
                System.out.println("5. Delete Book");
                System.out.print("Enter your choice : ");
                adminChoice = scanner.nextInt();
                scanner.nextLine();
                switch (adminChoice) {
                    case 1:
                        inventoryReport();
                        break;
                    case 2:
                        userReport();
                        break;
                    case 3:
                        addBook();
                        break;
                    case 4:
                        editBook();
                        break;
                    case 5:
                        deleteBook();
                        break;
                    default:
                        break;
                }
            }
                        
        }
    }

    public static void memberFunctions(){
        if(isLoggedIn && currentUser.getRole().equals("Member")){
            int memberChoice = -1;
            while(memberChoice != 0){
                System.out.println("\n---Member Dashboard---");
                System.out.println("0. Logout");
                System.out.println("1. View Profile");
                System.out.println("2. Search Book");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Pay Fine");
                System.out.print("Enter your choice : ");
                memberChoice = scanner.nextInt();
                scanner.nextLine();
                switch (memberChoice) {
                    case 1:
                        viewProfile();
                        break;
                    case 2:
                        searchBook();
                        break;
                    case 3:
                        borrowBook();
                        break;
                    case 4:
                        returnBook();
                        break;
                    case 5:
                        payFine();
                        break;
                    default:
                        break;
                }
            }
                        
        }
    }

    public static void viewProfile(){
        System.out.println("\n" + currentUser.getUserInfo());
    }

    public static void searchBook(){
        int searchChoice;
        System.out.println("1. Sort Book Alphabetically");
        System.out.println("2. Search Book by Title");
        System.out.println("3. Search Book by Author");
        System.out.println("4. Search Book by published Year");
        System.out.println("5. Search Book by Genre");
        System.out.println("Enter your choice : ");
        searchChoice = scanner.nextInt();
        scanner.nextLine();
        switch(searchChoice){
            case 1:
                sortBooksByTitle();
                for(Book b: books){
                    System.out.println("\n\n" + b.getBookInfo());
                }
                System.out.println("\n\n");
                break;
            case 2:
                searchByTitle();
                break;
            case 3:
                searchByAuthor();
                break;
            case 4:
                searchByYear();
                break;
            case 5:
                searchByGenre();
            case 0:
                System.out.println("Exiting...");
            default:
                System.out.println("Invalid Choice");
        }

    }

    public static void searchByTitle(){
        String titleInput;
        System.out.println("Search Book By Title");
        System.out.println("Enter Book Title");
        titleInput = scanner.nextLine();
        for(Book b : books){
            if(b.getTitle().equals(titleInput)){
                System.out.println("\n\n" + b.getBookInfo());
            }
        }
        System.out.println("\n\n");
    }

    public static void searchByAuthor(){
        String authorInput;
        System.out.println("Search Book By Author");
        System.out.println("Enter Author");
        authorInput = scanner.nextLine();
        for(Book b : books){
            if(b.getAuthor().equals(authorInput)){
                System.out.println("\n\n" + b.getBookInfo());
            }
        }
        System.out.println("\n\n");
    }

    public static void searchByYear(){
        int yearInput;
        System.out.println("Search Book By Published Year");
        System.out.println("Enter Published Year");
        yearInput = scanner.nextInt();
        scanner.nextLine();
        for(Book b : books){
            if(b.getPublicationYear() == yearInput){
                System.out.println("\n\n" + b.getBookInfo());
            }
        }
        System.out.println("\n\n");
    }
    
    public static void searchByGenre(){
        String genreInput;
        System.out.println("Search Book By Genre");
        System.out.println("Enter Genre");
        genreInput = scanner.nextLine();
        for(Book b : books){
            if(b.getGenre().equals(genreInput)){
                System.out.println("\n\n" + b.getBookInfo());
            }
        }
        System.out.println("\n\n");
    }


    //Implemented Using AI (Gemini Flash-Lite)
    public static void sortBooksByTitle() {
        books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        System.out.println("Books sorted alphabetically by title.");
    }
}
