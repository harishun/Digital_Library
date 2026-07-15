package models;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class Member extends User {
    private double balance;

    public Member(String userId, String name, String email, String phoneNumber, String password) {
        super(userId, name, email, phoneNumber, password, "Member");
        this.balance = 0.0;
    }

    public Member(String userId, String name, String email, String phoneNumber, String password, double balance) {
        super(userId, name, email, phoneNumber, password, "Member");
        this.balance = balance;
    }

    public double getBalance() { return balance; }

    public void addFine(double amount) {
        this.balance += amount;
    }

    public void payFine(double amount) {
        this.balance = Math.max(0.0, this.balance - amount);
    }

    @Override
    public String getUserInfo() {
        return "Member ID : " + this.userId + 
               "\nName : " + this.name + 
               "\nEmail : " + this.email + 
               "\nPhone Number : " + this.phoneNumber +
               "\nOutstanding Fine : $" + String.format("%.2f", this.balance);
    }

    public void viewProfile() {
        System.out.println("\n--- My Profile ---");
        System.out.println(this.getUserInfo());
        System.out.println("------------------\n");
    }

    public static void searchBook(ArrayList<Book> booksList, Scanner scanner) {
        System.out.println("\n--- Search Book ---");
        System.out.println("1. Sort Books Alphabetically");
        System.out.println("2. Search by Title");
        System.out.println("3. Search by Author");
        System.out.println("4. Search by Genre");
        System.out.println("5. Search by Publication Year");
        System.out.print("Enter search option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                ArrayList<Book> sorted = new ArrayList<>(booksList);
                sorted.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
                System.out.println("\nSorted Inventory:");
                for (Book b : sorted) {
                    System.out.println("\n" + b.getBookInfo());
                }
                break;
            case 2:
                System.out.print("Enter Title keyword: ");
                String title = scanner.nextLine().toLowerCase();
                for (Book b : booksList) {
                    if (b.getTitle().toLowerCase().contains(title)) {
                        System.out.println("\n" + b.getBookInfo());
                    }
                }
                break;
            case 3:
                System.out.print("Enter Author name: ");
                String author = scanner.nextLine().toLowerCase();
                for (Book b : booksList) {
                    if (b.getAuthor().toLowerCase().contains(author)) {
                        System.out.println("\n" + b.getBookInfo());
                    }
                }
                break;
            case 4:
                System.out.print("Enter Genre: ");
                String genre = scanner.nextLine().toLowerCase();
                for (Book b : booksList) {
                    if (b.getGenre() != null && b.getGenre().toLowerCase().contains(genre)) {
                        System.out.println("\n" + b.getBookInfo());
                    }
                }
                break;
            case 5:
                System.out.print("Enter Year: ");
                int year = scanner.nextInt();
                scanner.nextLine();
                for (Book b : booksList) {
                    if (b.getPublicationYear() == year) {
                        System.out.println("\n" + b.getBookInfo());
                    }
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    public void viewBorrowedBooks(ArrayList<Transaction> transactions) {
        System.out.println("\n--- Borrowed Books & Status ---");
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getMemberId().equals(this.userId)) {
                System.out.println("\n" + t.getTransactionDetails());
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have no active or completed borrow transactions.");
        }
    }

    public void borrowBook(ArrayList<Book> books, ArrayList<Transaction> transactions, Scanner scanner) {
        if (this.balance > 0.0) {
            System.out.println("Cannot borrow book. You have outstanding fines of $" + String.format("%.2f", this.balance) + ". Please pay them first.");
            return;
        }

        long activeBorrows = 0;
        for (Transaction t : transactions) {
            if (t.getMemberId().equals(this.userId) && t.getTransactionStatus().equals("Borrowed")) {
                activeBorrows++;
            }
        }

        if (activeBorrows >= 3) {
            System.out.println("Cannot borrow book. You have reached the borrow limit of 3 books.");
            return;
        }

        System.out.print("Enter book ISBN to borrow: ");
        String isbn = scanner.nextLine();
        Book targetBook = null;
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                targetBook = b;
                break;
            }
        }

        if (targetBook == null) {
            System.out.println("Book not found in library inventory.");
            return;
        }

        if (!targetBook.isAvailable()) {
            System.out.println("Book is currently borrowed by another member.");
            return;
        }

        int nextTxnId = transactions.size() + 1;
        String transactionId = "TXN-" + String.format("%04d", nextTxnId);
        Transaction newTxn = new Transaction(transactionId, this.userId, targetBook.getIsbn(), targetBook.getTitle());
        newTxn.borrowBook();
        targetBook.borrowBook();
        transactions.add(newTxn);

        System.out.println("Book \"" + targetBook.getTitle() + "\" successfully borrowed!");
        System.out.println("Due Date: " + newTxn.getDueDate());
    }

    public void returnBook(ArrayList<Book> books, ArrayList<Transaction> transactions, Scanner scanner) {
        System.out.print("Enter book ISBN to return: ");
        String isbn = scanner.nextLine();
        Transaction activeTxn = null;
        for (Transaction t : transactions) {
            if (t.getMemberId().equals(this.userId) && t.getBookIsbn().equals(isbn) && t.getTransactionStatus().equals("Borrowed")) {
                activeTxn = t;
                break;
            }
        }

        if (activeTxn == null) {
            System.out.println("No active borrowing transaction found for this ISBN.");
            return;
        }

        Book targetBook = null;
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                targetBook = b;
                break;
            }
        }

        activeTxn.returnBook();
        if (targetBook != null) {
            targetBook.returnBook();
        }

        System.out.println("Book returned successfully.");
        long overdue = activeTxn.calculateOverdueDays();
        if (overdue > 0) {
            double fineAmount = overdue * 1.0;
            this.addFine(fineAmount);
            System.out.println("WARNING: Book returned " + overdue + " days late!");
            System.out.println("Fine added to your account: $" + String.format("%.2f", fineAmount));
        } else {
            System.out.println("Thank you for returning on time!");
        }
    }
}
