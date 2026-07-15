package AI_Library_System;
import models.*;
import services.*;

import java.util.ArrayList;
import java.util.Scanner;

public class LibraryApp {
    private static final ArrayList<Book> books = new ArrayList<>();
    private static final ArrayList<User> users = new ArrayList<>();
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load data from persistent flat files
        StorageManager.loadAll(books, users, transactions);

        // If no users exist, bootstrap administrative credentials and sample data
        if (users.isEmpty()) {
            bootstrapData();
        }

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n===== AI DIGITAL LIBRARY SYSTEM =====");
            System.out.println("0. Exit Application");
            System.out.println("1. Register as a Member");
            System.out.println("2. Login");
            System.out.print("Enter choice: ");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerMember();
                    break;
                case 2:
                    login();
                    if (currentUser != null) {
                        if (currentUser.getRole().equals("Administrator")) {
                            librarianDashboard();
                        } else if (currentUser.getRole().equals("Member")) {
                            memberDashboard();
                        }
                    }
                    break;
                case 0:
                    System.out.println("Saving library state and shutting down... Goodbye!");
                    StorageManager.saveAll(books, users, transactions);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }

    private static void registerMember() {
        System.out.println("\n--- Member Registration ---");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        int nextMemberId = 1;
        for (User u : users) {
            if (u.getRole().equals("Member")) {
                int num = Integer.parseInt(u.getUserId().substring(4)); // format: "MEM-XXXX"
                if (num >= nextMemberId) {
                    nextMemberId = num + 1;
                }
            }
        }

        String userId = "MEM-" + String.format("%04d", nextMemberId);
        Member newMember = new Member(userId, name, email, phone, password);
        users.add(newMember);
        StorageManager.saveAll(books, users, transactions);
        System.out.println("Registration successful! Your generated User ID is: " + userId);
    }

    private static void login() {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User targetUser = null;
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(id)) {
                targetUser = u;
                break;
            }
        }

        if (targetUser == null || !targetUser.authenticate(password)) {
            System.out.println("Error: Incorrect User ID or Password.");
            return;
        }

        currentUser = targetUser;
        System.out.println("Login successful! Welcome back, " + currentUser.getName() + ".");

        // Automatically trigger system checks for overdue loans upon logging in
        if (currentUser.getRole().equals("Member")) {
            NotificationService.checkOverdueNotifications(currentUser, transactions);
        }
    }

    private static void memberDashboard() {
        Member member = (Member) currentUser;
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Member Dashboard (" + member.getName() + ") ---");
            System.out.println("0. Logout");
            System.out.println("1. View Profile Info");
            System.out.println("2. Search Book Inventory");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Pay Fine");
            System.out.println("6. View My Transaction History");
            System.out.print("Enter option: ");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    member.viewProfile();
                    break;
                case 2:
                    Member.searchBook(books, scanner);
                    break;
                case 3:
                    member.borrowBook(books, transactions, scanner);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 4:
                    member.returnBook(books, transactions, scanner);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 5:
                    payFineFlow(member);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 6:
                    member.viewBorrowedBooks(transactions);
                    break;
                case 0:
                    System.out.println("Logged out successfully.");
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void payFineFlow(Member member) {
        double fine = member.getBalance();
        if (fine <= 0.0) {
            System.out.println("You have no outstanding fine balance.");
            return;
        }
        System.out.println("Your current outstanding balance is: $" + String.format("%.2f", fine));
        System.out.print("Enter payment amount: $");
        if (!scanner.hasNextDouble()) {
            scanner.nextLine();
            System.out.println("Invalid amount.");
            return;
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (amount <= 0) {
            System.out.println("Payment amount must be greater than zero.");
            return;
        }

        member.payFine(amount);
        System.out.println("Payment of $" + String.format("%.2f", amount) + " processed successfully.");
        System.out.println("Your remaining fine balance is: $" + String.format("%.2f", member.getBalance()));
    }

    private static void librarianDashboard() {
        Librarian librarian = (Librarian) currentUser;
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Librarian Dashboard (" + librarian.getName() + ") ---");
            System.out.println("0. Logout");
            System.out.println("1. Generate Inventory Report");
            System.out.println("2. Generate User Report");
            System.out.println("3. Add New Book");
            System.out.println("4. Edit Book Genre");
            System.out.println("5. Remove Book");
            System.out.println("6. System Overdue Activity Monitor");
            System.out.println("7. Generate Transaction History Report");
            System.out.print("Enter option: ");
            if (!scanner.hasNextInt()) {
                scanner.nextLine();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    librarian.viewInventory(books);
                    break;
                case 2:
                    librarian.viewUsers(users);
                    break;
                case 3:
                    librarian.addBook(books, scanner);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 4:
                    librarian.editBook(books, scanner);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 5:
                    librarian.deleteBook(books, scanner);
                    StorageManager.saveAll(books, users, transactions);
                    break;
                case 6:
                    NotificationService.displaySystemOverdueAlerts(transactions);
                    break;
                case 7:
                    librarian.viewTransaction(transactions);
                    break;
                case 0:
                    System.out.println("Logged out successfully.");
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void bootstrapData() {
        System.out.println("Initializing system database with default credentials...");
        users.add(new Librarian("ADM-0001", "Librarian Admin", "admin@library.com", "999-888-7777", "admin"));
        users.add(new Member("MEM-0001", "John Doe", "john@example.com", "555-666-7777", "1234"));
        users.add(new Member("MEM-0002", "David Bosch", "david@example.com", "555-444-3333", "Pass"));

        books.add(new Book("978-0134685991", "Effective Java", "Joshua Bloch", "Education", 2018));
        books.add(new Book("978-0201633610", "Design Patterns", "Erich Gamma", "Software Engineering", 1994));
        books.add(new Book("978-0596007126", "Head First Design Patterns", "Eric Freeman", "Education", 2004));
        books.add(new Book("978-0132350884", "Clean Code", "Robert C. Martin", "Software Engineering", 2008));
        books.add(new Book("978-0201616224", "The Pragmatic Programmer", "Andrew Hunt", "Software Engineering", 1999));
        
        StorageManager.saveAll(books, users, transactions);
    }
}
