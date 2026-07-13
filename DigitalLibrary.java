import java.util.Scanner;
import java.util.ArrayList;
import java.io.*; // Implemented using Antigravity AI (Gemini)
import java.time.LocalDate; // Implemented using Antigravity AI (Gemini)


public class DigitalLibrary {
    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Transaction> transactions = new ArrayList<>(); // Implemented using Antigravity AI (Gemini)
    static User currentUser = null;
    static boolean isLoggedIn = false;
    static Scanner scanner = new Scanner(System.in);

    // START: Implemented using Antigravity AI (Gemini)
    private static final String USERS_FILE = "data_users.txt";
    private static final String BOOKS_FILE = "data_books.txt";
    private static final String TRANSACTIONS_FILE = "data_transactions.txt";
    // END: Implemented using Antigravity AI (Gemini)

    public static void main(String[] args) {
        // START: Implemented using Antigravity AI (Gemini)
        loadData();
        // END: Implemented using Antigravity AI (Gemini)
        
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
        // START: Implemented using Antigravity AI (Gemini)
        saveData();
        // END: Implemented using Antigravity AI (Gemini)
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
                    checkNotifications(); // Implemented using Antigravity AI (Gemini)
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
            // START: Implemented using Antigravity AI (Gemini)
            saveData();
            // END: Implemented using Antigravity AI (Gemini)
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
                // START: Implemented using Antigravity AI (Gemini)
                saveData();
                // END: Implemented using Antigravity AI (Gemini)
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
                // START: Implemented using Antigravity AI (Gemini)
                saveData();
                break; // Fix ConcurrentModificationException
                // END: Implemented using Antigravity AI (Gemini)
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
                System.out.println("6. Generate Overdue Books Report"); // Implemented using Antigravity AI (Gemini)
                System.out.println("7. Generate Transaction History Report"); // Implemented using Antigravity AI (Gemini)
                System.out.print("Enter your choice : ");
                adminChoice = scanner.nextInt();
                scanner.nextLine();
                switch (adminChoice) {
                    case 0: // Implemented using Antigravity AI (Gemini)
                        isLoggedIn = false; // Implemented using Antigravity AI (Gemini)
                        currentUser = null; // Implemented using Antigravity AI (Gemini)
                        System.out.println("Logged out successfully."); // Implemented using Antigravity AI (Gemini)
                        break; // Implemented using Antigravity AI (Gemini)
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
                    case 6: // Implemented using Antigravity AI (Gemini)
                        generateOverdueReport(); // Implemented using Antigravity AI (Gemini)
                        break; // Implemented using Antigravity AI (Gemini)
                    case 7: // Implemented using Antigravity AI (Gemini)
                        generateTransactionReport(); // Implemented using Antigravity AI (Gemini)
                        break; // Implemented using Antigravity AI (Gemini)
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
                System.out.println("6. View Transaction History"); // Implemented using Antigravity AI (Gemini)
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
                    case 6: // Implemented using Antigravity AI (Gemini)
                        viewMemberTransactionHistory(); // Implemented using Antigravity AI (Gemini)
                        break; // Implemented using Antigravity AI (Gemini)
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

    public static void borrowBook(){
        if (currentUser == null || !currentUser.getRole().equals("Member")) {
            System.out.println("You must be logged in as a member to borrow books.");
            return;
        }
        Member member = (Member) currentUser;
        if (member.getBalance() > 0.0) {
            System.out.println("Cannot borrow book. You have outstanding fines of $" + String.format("%.2f", member.getBalance()) + ". Please clear your fines first.");
            return;
        }
        
        long activeBorrowsCount = 0;
        for (Transaction t : transactions) {
            if (t.getMember().getUserId().equals(member.getUserId()) && t.getTransactionStatus().equals("Borrowed")) {
                activeBorrowsCount++;
            }
        }
        if (activeBorrowsCount >= 3) {
            System.out.println("Cannot borrow book. You have reached the borrow limit of 3 books.");
            return;
        }

        System.out.print("Enter book ISBN to borrow: ");
        String isbnInput = scanner.nextLine();
        Book targetBook = null;
        for (Book b : books) {
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
        for (Transaction t : transactions) {
            try {
                int num = Integer.parseInt(t.getTransactionId().replace("TXN-", ""));
                if (num > maxTxnNum) maxTxnNum = num;
            } catch (Exception e) {}
        }
        String transactionId = "TXN-" + String.format("%04d", maxTxnNum + 1);
        Transaction newTxn = new Transaction(transactionId, member, targetBook);
        newTxn.borrowBook();
        transactions.add(newTxn);
        System.out.println("Book \"" + targetBook.getTitle() + "\" borrowed successfully!");
        System.out.println("Transaction ID: " + transactionId + " | Due Date: " + newTxn.getEstimatedReturnDate());
        saveData();
    }

    // START: Implemented using Antigravity AI (Gemini)
    public static void returnBook() {
        if (currentUser == null || !currentUser.getRole().equals("Member")) {
            System.out.println("You must be logged in as a member to return books.");
            return;
        }
        Member member = (Member) currentUser;
        System.out.print("Enter book ISBN to return: ");
        String isbnInput = scanner.nextLine();
        Transaction activeTxn = null;
        for (Transaction t : transactions) {
            if (t.getMember().getUserId().equals(member.getUserId()) &&
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
            double fineAmount = overdue * 1.0; // fine rate: $1.00 per day
            member.addFine(fineAmount);
            System.out.println("WARNING: Book was returned " + overdue + " days late!");
            System.out.println("A fine of $" + String.format("%.2f", fineAmount) + " has been added to your account.");
        } else {
            System.out.println("Returned on time. Thank you!");
        }
        saveData();
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
        member.payFineAmount(paymentAmount);
        System.out.println("Payment of $" + String.format("%.2f", paymentAmount) + " processed successfully.");
        System.out.println("Your new fine balance is: $" + String.format("%.2f", member.getBalance()));
        saveData();
    }
    // END: Implemented using Antigravity AI (Gemini)

    // START: Implemented using Antigravity AI (Gemini)
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
                        System.out.println("[NOTIFICATION] WARNING: The book \"" + t.getBook().getTitle() + "\" (ISBN: " + t.getBook().getISBN() + ") was due on " + t.getEstimatedReturnDate() + " and is OVERDUE by " + overdue + " days!");
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
        System.out.println("\n--- All System Transactions ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions have been recorded yet.");
        } else {
            for (Transaction t : transactions) {
                System.out.println("\n" + t.getTransaction());
                System.out.println("----------------------------");
            }
        }
        System.out.println("-------------------------------\n");
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
        
        saveData();
    }

    public static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                double bal = 0.0;
                if (u instanceof Member) {
                    bal = ((Member) u).getBalance();
                }
                writer.write(u.getUserId() + "|" + u.getName() + "|" + u.getEmail() + "|" + u.getPhoneNumber() + "|" + u.password + "|" + u.getRole() + "|" + bal);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : books) {
                String genreStr = (b.getGenre() != null) ? b.getGenre() : "null";
                writer.write(b.getISBN() + "|" + b.getTitle() + "|" + b.getAuthor() + "|" + genreStr + "|" + b.getPublicationYear() + "|" + b.isAvailable());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction t : transactions) {
                String bDate = (t.getBorrowedDate() != null) ? t.getBorrowedDate().toString() : "null";
                String eDate = (t.getEstimatedReturnDate() != null) ? t.getEstimatedReturnDate().toString() : "null";
                String rDate = (t.getReturnDate() != null) ? t.getReturnDate().toString() : "null";
                writer.write(t.getTransactionId() + "|" + t.getMember().getUserId() + "|" + t.getBook().getISBN() + "|" + bDate + "|" + eDate + "|" + rDate + "|" + t.getTransactionStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }

    public static void loadData() {
        File userFile = new File(USERS_FILE);
        if (!userFile.exists()) {
            bootstrapData();
            return;
        }

        int maxMem = 0;
        int maxAdmin = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length < 7) continue;
                String userId = parts[0];
                String name = parts[1];
                String email = parts[2];
                String phoneNumber = parts[3];
                String password = parts[4];
                String role = parts[5];
                double balance = Double.parseDouble(parts[6]);

                if (role.equals("Administrator")) {
                    int num = Integer.parseInt(userId.replace("ADM-", ""));
                    if (num > maxAdmin) maxAdmin = num;
                    Administrator.setAdminNo(num);
                    Administrator admin = new Administrator(name, email, phoneNumber, password);
                    users.add(admin);
                } else if (role.equals("Member")) {
                    int num = Integer.parseInt(userId.replace("MEM-", ""));
                    if (num > maxMem) maxMem = num;
                    Member.setMemberNo(num);
                    Member member = new Member(name, email, phoneNumber, password);
                    member.addFine(balance);
                    users.add(member);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        Member.setMemberNo(maxMem + 1);
        Administrator.setAdminNo(maxAdmin + 1);

        File bookFile = new File(BOOKS_FILE);
        if (bookFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(bookFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 6) continue;
                    String isbn = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String genre = parts[3].equals("null") ? null : parts[3];
                    int year = Integer.parseInt(parts[4]);
                    boolean isAvailable = Boolean.parseBoolean(parts[5]);

                    Book book = new Book(isbn, title, author, year, genre);
                    book.setAvailability(isAvailable);
                    books.add(book);
                }
            } catch (IOException e) {
                System.err.println("Error loading books: " + e.getMessage());
            }
        }

        File txnFile = new File(TRANSACTIONS_FILE);
        if (txnFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(txnFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split("\\|");
                    if (parts.length < 7) continue;
                    String txnId = parts[0];
                    String memberId = parts[1];
                    String bookIsbn = parts[2];
                    String bDateStr = parts[3];
                    String eDateStr = parts[4];
                    String rDateStr = parts[5];
                    String status = parts[6];

                    Member member = null;
                    for (User u : users) {
                        if (u.getUserId().equals(memberId) && u instanceof Member) {
                            member = (Member) u;
                            break;
                        }
                    }

                    Book book = null;
                    for (Book b : books) {
                        if (b.getISBN().equals(bookIsbn)) {
                            book = b;
                            break;
                        }
                    }

                    if (member != null && book != null) {
                        Transaction txn = new Transaction(txnId, member, book);
                        LocalDate bDate = bDateStr.equals("null") ? null : LocalDate.parse(bDateStr);
                        LocalDate eDate = eDateStr.equals("null") ? null : LocalDate.parse(eDateStr);
                        LocalDate rDate = rDateStr.equals("null") ? null : LocalDate.parse(rDateStr);
                        txn.loadTransactionState(bDate, eDate, rDate, status);
                        transactions.add(txn);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading transactions: " + e.getMessage());
            }
        }
    }

    public static void viewMemberTransactionHistory() {
        if (currentUser == null || !currentUser.getRole().equals("Member")) {
            System.out.println("You must be logged in as a member to view transaction history.");
            return;
        }
        Member member = (Member) currentUser;
        System.out.println("\n--- Your Transaction History ---");
        boolean hasTxn = false;
        for (Transaction t : transactions) {
            if (t.getMember().getUserId().equals(member.getUserId())) {
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
    // END: Implemented using Antigravity AI (Gemini)

    //Implemented Using AI (Gemini Flash-Lite)
    public static void sortBooksByTitle() {
        books.sort((b1, b2) -> b1.getTitle().compareToIgnoreCase(b2.getTitle()));
        System.out.println("Books sorted alphabetically by title.");
    }
}
