package models;

import java.util.ArrayList;
import java.util.Scanner;

public class Librarian extends User {

    public Librarian(String userId, String name, String email, String phoneNumber, String password) {
        super(userId, name, email, phoneNumber, password, "Administrator");
    }

    @Override
    public String getUserInfo() {
        return "Librarian ID : " + this.userId + 
               "\nName : " + this.name + 
               "\nEmail : " + this.email + 
               "\nPhone Number : " + this.phoneNumber;
    }

    public void viewInventory(ArrayList<Book> books) {
        System.out.println("\n--- Library Inventory Report ---");
        if (books.isEmpty()) {
            System.out.println("The library inventory is empty.");
            return;
        }
        for (Book b : books) {
            System.out.println("\n" + b.getBookInfo());
            System.out.println("--------------------");
        }
    }

    public void viewUsers(ArrayList<User> users) {
        System.out.println("\n--- User Directory Report ---");
        for (User u : users) {
            System.out.println("\n" + u.getUserInfo());
            System.out.println("--------------------");
        }
    }

    public void addBook(ArrayList<Book> books, Scanner scanner) {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();
        
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                System.out.println("Error: A book with ISBN " + isbn + " already exists.");
                return;
            }
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter Publication Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        Book newBook = new Book(isbn, title, author, genre, year);
        books.add(newBook);
        System.out.println("Book \"" + title + "\" successfully added to the collection!");
    }

    public void editBook(ArrayList<Book> books, Scanner scanner) {
        System.out.println("\n--- Edit Book Genre ---");
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        Book targetBook = null;
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                targetBook = b;
                break;
            }
        }

        if (targetBook == null) {
            System.out.println("Book with ISBN " + isbn + " not found.");
            return;
        }

        System.out.println("Current Genre: " + targetBook.getGenre());
        System.out.print("Enter New Genre: ");
        String newGenre = scanner.nextLine();
        targetBook.setGenre(newGenre);
        System.out.println("Book details updated successfully.");
    }

    public void deleteBook(ArrayList<Book> books, Scanner scanner) {
        System.out.println("\n--- Remove Book from Catalog ---");
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        Book targetBook = null;
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                targetBook = b;
                break;
            }
        }

        if (targetBook == null) {
            System.out.println("Book with ISBN " + isbn + " not found.");
            return;
        }

        if (!targetBook.isAvailable()) {
            System.out.println("Cannot delete book. It is currently borrowed.");
            return;
        }

        books.remove(targetBook);
        System.out.println("Book \"" + targetBook.getTitle() + "\" successfully deleted.");
    }

    public void viewTransaction(ArrayList<Transaction> transactions) {
        System.out.println("\n--- All System Transactions ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions have been recorded yet.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println("\n" + t.getTransactionDetails());
            System.out.println("--------------------");
        }
    }
}
