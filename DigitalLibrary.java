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

        books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Education", 2018));
        books.add(new Book("978-0201633610", "Design Patterns", "Erich Gamma", "Software Engineering", 1994));
        books.add(new Book("978-0596007126", "Head First Design Patterns", "Eric Freeman", "Education", 2004));
        books.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Software Engineering", 2008));
        books.add(new Book("978-0201616224", "The Pragmatic Programmer", "Andrew Hunt", "Software Engineering", 1999));
        
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
                    if(isLoggedIn && currentUser.getRole().equals("Administrator")){
                        int adminChoice = -1;
                        while(adminChoice != 0){
                            System.out.println("\n---Admin Dashboard---");
                            System.out.println("0. Logout");
                            System.out.println("1. Generate Inventory Report");
                            System.out.println("2. Generate User Report");
                            System.out.println("Enter your choice:");
                            adminChoice = scanner.nextInt();
                            scanner.nextLine();
                            switch (adminChoice) {
                                case 1:
                                    inventoryReport();
                                    break;
                                case 2:
                                    userReport();
                                default:
                                    break;
                            }
                        }
                        
                    }
                    break;
                case 0:
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
            String genre = null;
            System.out.print("Enter book genre (optional, press Enter to skip): ");
            String genreInput = scanner.nextLine();
            if (!genreInput.isEmpty()) {
                genre = genreInput;
            }
            Integer year = null;
            System.out.print("Enter publication year: (optional, press Enter to skip): ");
            int yearInput = scanner.nextInt();
            if (yearInput > 0) {
                year = yearInput;
            }
            Book newBook = new Book(isbn, title, author, genre, year);
            books.add(newBook);
            System.out.println("Book added successfully.");
        } else {
            System.out.println("You must be logged in as an administrator to add books.");
        }
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

}