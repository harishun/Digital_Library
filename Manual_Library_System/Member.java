package Manual_Library_System;
import java.util.ArrayList;

class Member extends User {
    static private int memberNo = 1;
    private double balance = 0.0;
    
    Member(String name, String email, String phoneNumber, String password){
        super("MEM-" + memberNo++, name, email, phoneNumber, password, "Member");
    }
    
    public double getBalance() { 
        return this.balance; 
    }
    
    public static void setMemberNo(int nextNo) {
        memberNo = nextNo;
    }

    public void addFine(double amount) { 
        this.balance += amount; 
    }
    
    public void payFine(double amount) {
        this.balance = Math.max(0.0, this.balance - amount);
    }

    public void viewProfile() {
        System.out.println("\n" + this.getUserInfo());
        System.out.println("Outstanding Balance: $" + String.format("%.2f", this.balance));
    }

    public static void searchBook(ArrayList<Book> booksList) {
        int searchChoice;
        System.out.println("1. Sort Book Alphabetically");
        System.out.println("2. Search Book by Title");
        System.out.println("3. Search Book by Author");
        System.out.println("4. Search Book by published Year");
        System.out.println("5. Search Book by Genre");
        System.out.print("Enter your choice: ");
        searchChoice = DigitalLibrary.scanner.nextInt();
        DigitalLibrary.scanner.nextLine();
        switch(searchChoice){
            case 1:
                booksList.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
                System.out.println("Books sorted alphabetically by title.");
                for(Book b: booksList){
                    System.out.println("\n\n" + b.getBookInfo());
                }
                System.out.println("\n\n");
                break;
            case 2:
                System.out.println("Search Book By Title");
                System.out.print("Enter Book Title: ");
                String titleInput = DigitalLibrary.scanner.nextLine();
                for(Book b : booksList){
                    if(b.getTitle().equalsIgnoreCase(titleInput)){
                        System.out.println("\n\n" + b.getBookInfo());
                    }
                }
                System.out.println("\n\n");
                break;
            case 3:
                System.out.println("Search Book By Author");
                System.out.print("Enter Author: ");
                String authorInput = DigitalLibrary.scanner.nextLine();
                for(Book b : booksList){
                    if(b.getAuthor().equalsIgnoreCase(authorInput)){
                        System.out.println("\n\n" + b.getBookInfo());
                    }
                }
                System.out.println("\n\n");
                break;
            case 4:
                System.out.println("Search Book By Published Year");
                System.out.print("Enter Published Year: ");
                int yearInput = DigitalLibrary.scanner.nextInt();
                DigitalLibrary.scanner.nextLine();
                for(Book b : booksList){
                    if(b.getPublicationYear() == yearInput){
                        System.out.println("\n\n" + b.getBookInfo());
                    }
                }
                System.out.println("\n\n");
                break;
            case 5:
                System.out.println("Search Book By Genre");
                System.out.print("Enter Genre: ");
                String genreInput = DigitalLibrary.scanner.nextLine();
                for(Book b : booksList){
                    if(b.getGenre() != null && b.getGenre().equalsIgnoreCase(genreInput)){
                        System.out.println("\n\n" + b.getBookInfo());
                    }
                }
                System.out.println("\n\n");
                break;
            case 0:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid Choice");
        }
    }

    public void viewBorrowedBooks(ArrayList<Transaction> transactionsList) {
        System.out.println("\n--- Your Transaction History ---");
        boolean hasTxn = false;
        for (Transaction t : transactionsList) {
            if (t.getMember().getUserId().equals(this.getUserId())) {
                System.out.println("\n" + t.getTransaction());
                System.out.println("--------------------------------");
                hasTxn = true;
            }
        }
        if (!hasTxn) {
            System.out.println("You have no recorded transactions.");
        }
        System.out.println("--------------------------------\n");
    }

    public void registerMember() {
        if (!DigitalLibrary.users.contains(this)) {
            DigitalLibrary.users.add(this);
        }
    }

    public void borrowBook(ArrayList<Book> booksList) {
        if (this.balance > 0.0) {
            System.out.println("Cannot borrow book. You have outstanding fines of $" + String.format("%.2f", this.balance) + ". Please clear your fines first.");
            return;
        }
        
        long activeBorrowsCount = 0;
        for (Transaction t : DigitalLibrary.transactions) {
            if (t.getMember().getUserId().equals(this.getUserId()) && t.getTransactionStatus().equals("Borrowed")) {
                activeBorrowsCount++;
            }
        }
        if (activeBorrowsCount >= 3) {
            System.out.println("Cannot borrow book. You have reached the borrow limit of 3 books.");
            return;
        }

        System.out.print("Enter book ISBN to borrow: ");
        String isbnInput = DigitalLibrary.scanner.nextLine();
        Book targetBook = null;
        for (Book b : booksList) {
            if (b.getISBN().equals(isbnInput)) {
                targetBook = b;
                break;
            }
        }
        if (targetBook == null) {
            System.out.println("Book with ISBN " + isbnInput + " does not exist.");
            return;
        }
        if (!targetBook.isAvailable()) {
            System.out.println("Book \"" + targetBook.getTitle() + "\" is currently unavailable (already borrowed).");
            return;
        }

        int maxTxnNum = 0;
        for (Transaction t : DigitalLibrary.transactions) {
            try {
                int num = Integer.parseInt(t.getTransactionId().replace("TXN-", ""));
                if (num > maxTxnNum) maxTxnNum = num;
            } catch (Exception e) {}
        }
        String transactionId = "TXN-" + String.format("%04d", maxTxnNum + 1);
        Transaction newTxn = new Transaction(transactionId, this, targetBook);
        newTxn.borrowBook();
        DigitalLibrary.transactions.add(newTxn);
        System.out.println("Book \"" + targetBook.getTitle() + "\" borrowed successfully!");
        System.out.println("Transaction ID: " + transactionId + " | Due Date: " + newTxn.getDueDate());
    }

    public void returnBook(ArrayList<Book> booksList) {
        System.out.print("Enter book ISBN to return: ");
        String isbnInput = DigitalLibrary.scanner.nextLine();
        Transaction activeTxn = null;
        for (Transaction t : DigitalLibrary.transactions) {
            if (t.getMember().getUserId().equals(this.getUserId()) &&
                t.getBook().getISBN().equals(isbnInput) &&
                t.getTransactionStatus().equals("Borrowed")) {
                activeTxn = t;
                break;
            }
        }
        if (activeTxn == null) {
            System.out.println("No active borrowing transaction found for ISBN " + isbnInput + " under your account.");
            return;
        }

        activeTxn.returnBook();
        System.out.println("Book \"" + activeTxn.getBook().getTitle() + "\" returned successfully.");
        long overdue = activeTxn.overdueDays();
        if (overdue > 0) {
            double fineAmount = overdue * 1.0;
            this.addFine(fineAmount);
            System.out.println("WARNING: Book was returned " + overdue + " days late!");
            System.out.println("A fine of $" + String.format("%.2f", fineAmount) + " has been added to your account.");
        } else {
            System.out.println("Returned on time. Thank you!");
        }
    }
}