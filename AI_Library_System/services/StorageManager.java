package services;

import models.Book;
import models.User;
import models.Member;
import models.Librarian;
import models.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class StorageManager {
    private static final String DATA_DIR = "data";
    private static final String BOOKS_FILE = DATA_DIR + "/books.txt";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.txt";

    public static void ensureDirectoriesExist() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void saveAll(ArrayList<Book> books, ArrayList<User> users, ArrayList<Transaction> transactions) {
        ensureDirectoriesExist();

        // 1. Save Books
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : books) {
                writer.write(b.getIsbn() + "|" + 
                             b.getTitle() + "|" + 
                             b.getAuthor() + "|" + 
                             b.getGenre() + "|" + 
                             b.getPublicationYear() + "|" + 
                             b.isAvailable());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }

        // 2. Save Users
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                double balance = 0.0;
                if (u.getRole().equals("Member")) {
                    balance = ((Member) u).getBalance();
                }
                writer.write(u.getUserId() + "|" + 
                             u.getName() + "|" + 
                             u.getEmail() + "|" + 
                             u.getPhoneNumber() + "|" + 
                             u.getPassword() + "|" + 
                             u.getRole() + "|" + 
                             balance);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }

        // 3. Save Transactions
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                String bDate = (t.getBorrowedDate() != null) ? t.getBorrowedDate().toString() : "null";
                String dDate = (t.getDueDate() != null) ? t.getDueDate().toString() : "null";
                String rDate = (t.getReturnDate() != null) ? t.getReturnDate().toString() : "null";
                
                writer.write(t.getTransactionId() + "|" + 
                             t.getMemberId() + "|" + 
                             t.getBookIsbn() + "|" + 
                             t.getBookTitle() + "|" + 
                             bDate + "|" + 
                             dDate + "|" + 
                             rDate + "|" + 
                             t.getTransactionStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public static void loadAll(ArrayList<Book> books, ArrayList<User> users, ArrayList<Transaction> transactions) {
        ensureDirectoriesExist();

        // 1. Load Books
        File bookFile = new File(BOOKS_FILE);
        if (bookFile.exists()) {
            books.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 6) continue;
                    String isbn = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String genre = parts[3];
                    int year = Integer.parseInt(parts[4]);
                    boolean available = Boolean.parseBoolean(parts[5]);
                    
                    books.add(new Book(isbn, title, author, genre, year, available));
                }
            } catch (IOException e) {
                System.err.println("Error loading books: " + e.getMessage());
            }
        }

        // 2. Load Users
        File userFile = new File(USERS_FILE);
        if (userFile.exists()) {
            users.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 7) continue;
                    String userId = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    String phone = parts[3];
                    String password = parts[4];
                    String role = parts[5];
                    double balance = Double.parseDouble(parts[6]);

                    if (role.equalsIgnoreCase("Administrator")) {
                        users.add(new Librarian(userId, name, email, phone, password));
                    } else {
                        users.add(new Member(userId, name, email, phone, password, balance));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading users: " + e.getMessage());
            }
        }

        // 3. Load Transactions
        File txnFile = new File(TRANSACTIONS_FILE);
        if (txnFile.exists()) {
            transactions.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(txnFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 8) continue;
                    String txnId = parts[0];
                    String memberId = parts[1];
                    String isbn = parts[2];
                    String title = parts[3];
                    LocalDate bDate = parts[4].equals("null") ? null : LocalDate.parse(parts[4]);
                    LocalDate dDate = parts[5].equals("null") ? null : LocalDate.parse(parts[5]);
                    LocalDate rDate = parts[6].equals("null") ? null : LocalDate.parse(parts[6]);
                    String status = parts[7];

                    transactions.add(new Transaction(txnId, memberId, isbn, title, bDate, dDate, rDate, status));
                }
            } catch (IOException e) {
                System.err.println("Error loading transactions: " + e.getMessage());
            }
        }
    }
}
