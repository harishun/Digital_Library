package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookIsbn;
    private String bookTitle;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String transactionStatus;

    public Transaction(String transactionId, String memberId, String bookIsbn, String bookTitle) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.transactionStatus = "Pending";
    }

    public Transaction(String transactionId, String memberId, String bookIsbn, String bookTitle, 
                       LocalDate borrowedDate, LocalDate dueDate, LocalDate returnDate, String transactionStatus) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.bookTitle = bookTitle;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionId() { return transactionId; }
    public String getMemberId() { return memberId; }
    public String getBookIsbn() { return bookIsbn; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getTransactionStatus() { return transactionStatus; }

    public void borrowBook() {
        this.borrowedDate = LocalDate.now();
        this.dueDate = this.borrowedDate.plusDays(7); // standard 7-day borrow period
        this.transactionStatus = "Borrowed";
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.transactionStatus = "Returned";
    }

    public long calculateOverdueDays() {
        LocalDate dateToCompare;
        if (returnDate != null) {
            dateToCompare = returnDate;
        } else {
            dateToCompare = LocalDate.now();
        }

        if (dateToCompare.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, dateToCompare);
        }
        return 0;
    }

    public String getTransactionDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String borrowedStr;
        if (borrowedDate != null) {
            borrowedStr = borrowedDate.format(formatter);
        } else {
            borrowedStr = "N/A";
        }

        String returnedStr;
        if (returnDate != null) {
            returnedStr = returnDate.format(formatter);
        } else {
            returnedStr = "Not yet Returned";
        }

        String details = "Transaction ID: " + this.transactionId +
                         "\nBook: " + this.bookTitle + " (ISBN: " + this.bookIsbn + ")" +
                         "\nMember ID: " + this.memberId +
                         "\nBorrowed Date: " + borrowedStr +
                         "\nDue Date: " + (dueDate != null ? dueDate.format(formatter) : "N/A") +
                         "\nReturned Date: " + returnedStr +
                         "\nStatus: " + this.transactionStatus;

        long overdue = calculateOverdueDays();
        if (overdue > 0 && !this.transactionStatus.equals("Returned")) {
            details += "\n*** OVERDUE BY " + overdue + " DAYS ***";
        }
        return details;
    }
}
