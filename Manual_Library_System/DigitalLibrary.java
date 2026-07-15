package Manual_Library_System;
import java.util.Scanner;
import java.util.ArrayList;

public class DigitalLibrary {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Book> books = new ArrayList<>();
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    public static User currentUser = null;
    static boolean isLoggedIn = false;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        bootstrapData();
        
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
        newMember.registerMember();
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
                    checkNotifications();
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
        if (isLoggedIn && currentUser instanceof Administrator) {
            ((Administrator) currentUser).addBook(books);
        } else {
            System.out.println("You must be logged in as an administrator to add books.");
        }
    }

    public static void inventoryReport(){
        if (currentUser instanceof Administrator) {
            ((Administrator) currentUser).viewInventory();
        }
    }

    public static void userReport(){
        if (currentUser instanceof Administrator) {
            ((Administrator) currentUser).viewUsers();
        }
    }

    public static void editBook(){
        if (currentUser instanceof Administrator) {
            ((Administrator) currentUser).editBook(books);
        }
    }

    public static void deleteBook(){
        if (currentUser instanceof Administrator) {
            ((Administrator) currentUser).deleteBook(books);
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
                System.out.println("6. Generate Overdue Books Report");
                System.out.println("7. Generate Transaction History Report");
                System.out.print("Enter your choice : ");
                adminChoice = scanner.nextInt();
                scanner.nextLine();
                switch (adminChoice) {
                    case 0:
                        isLoggedIn = false;
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                        break;
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
                    case 6:
                        generateOverdueReport();
                        break;
                    case 7:
                        generateTransactionReport();
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
                System.out.println("6. View Transaction History");
                System.out.print("Enter your choice : ");
                memberChoice = scanner.nextInt();
                scanner.nextLine();
                switch (memberChoice) {
                    case 0:
                        isLoggedIn = false;
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                        break;
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
                    case 6:
                        viewMemberTransactionHistory();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public static void viewProfile(){
        if (currentUser instanceof Member) {
            ((Member) currentUser).viewProfile();
        } else if (currentUser != null) {
            System.out.println("\n" + currentUser.getUserInfo());
        }
    }

    public static void searchBook(){
        if (currentUser instanceof Member) {
            Member.searchBook(books);
        }
    }

    public static void borrowBook(){
        if (currentUser instanceof Member) {
            ((Member) currentUser).borrowBook(books);
        }
    }

    public static void returnBook() {
        if (currentUser instanceof Member) {
            ((Member) currentUser).returnBook(books);
        }
    }

    public static void payFine() {
        if (currentUser == null || !currentUser.getRole().equals("Member")) {
            System.out.println("You must be logged in as a member to pay fines.");
            return;
        }
        Member member = (Member) currentUser;
        double currentBalance = member.getBalance();
        if (currentBalance <= 0.0) {
            System.out.println("You have no outstanding fine balance.");
            return;
        }
        System.out.println("Your current outstanding fine balance is: $" + String.format("%.2f", currentBalance));
        System.out.print("Enter payment amount: $");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();
        if (paymentAmount <= 0.0) {
            System.out.println("Invalid payment amount. Payment must be greater than 0.");
            return;
        }
        member.payFine(paymentAmount);
        System.out.println("Payment of $" + String.format("%.2f", paymentAmount) + " processed successfully.");
        System.out.println("Your new fine balance is: $" + String.format("%.2f", member.getBalance()));
    }

    public static void checkNotifications() {
        if (currentUser != null && currentUser.getRole().equals("Member")) {
            Member member = (Member) currentUser;
            boolean hasNotifications = false;
            if (member.getBalance() > 0.0) {
                System.out.println("\n[NOTIFICATION] You have an outstanding fine balance of $" + String.format("%.2f", member.getBalance()) + ". Please pay your fine to restore borrowing privileges.");
                hasNotifications = true;
            }
            for (Transaction t : transactions) {
                if (t.getMember().getUserId().equals(member.getUserId()) && t.getTransactionStatus().equals("Borrowed")) {
                    long overdue = t.overdueDays();
                    if (overdue > 0) {
                        System.out.println("[NOTIFICATION] WARNING: The book \"" + t.getBook().getTitle() + "\" (ISBN: " + t.getBook().getISBN() + ") was due on " + t.getDueDate() + " and is OVERDUE by " + overdue + " days!");
                        hasNotifications = true;
                    }
                }
            }
            if (!hasNotifications) {
                System.out.println("\n[NOTIFICATION] You have no overdue books or outstanding fines.");
            }
        }
    }

    public static void generateOverdueReport() {
        System.out.println("\n--- Overdue Books Report ---");
        boolean hasOverdue = false;
        for (Transaction t : transactions) {
            if (t.getTransactionStatus().equals("Borrowed") && t.overdueDays() > 0) {
                System.out.println("\n" + t.getTransaction());
                System.out.println("Days Overdue: " + t.overdueDays());
                System.out.println("Estimated Fine: $" + String.format("%.2f", t.overdueDays() * 1.0));
                hasOverdue = true;
            }
        }
        if (!hasOverdue) {
            System.out.println("No overdue books at the moment.");
        }
        System.out.println("----------------------------\n");
    }

    public static void generateTransactionReport() {
        if (currentUser instanceof Administrator) {
            ((Administrator) currentUser).viewTransaction();
        }
    }

    public static void bootstrapData() {
        users.add(new Administrator("admin", "example@gmail.com", "0123456789", "admin"));
        users.add(new Member("John Doe", "example@gmail.com", "01233445453", "1234"));
        users.add(new Member("David Bosch", "example@gmail.com", "01233445453", "Pass"));

        books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018, "Education"));
        books.add(new Book("978-0201633610", "Design Patterns", "Erich Gamma", 1994, "Software Engineering"));
        books.add(new Book("978-0596007126", "Head First Design Patterns", "Eric Freeman", 2004, "Education"));
        books.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Software Engineering"));
        books.add(new Book("978-0201616224", "The Pragmatic Programmer", "Andrew Hunt", 1999, "Software Engineering"));
    }

    public static void viewMemberTransactionHistory() {
        if (currentUser instanceof Member) {
            ((Member) currentUser).viewBorrowedBooks(transactions);
        }
    }
}
