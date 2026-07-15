import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Transaction {
    private String transactionId;
    private Member member;
    private Book book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String transactionStatus;
    private final int DEFAULT_BORROW_DAYS = 7;

    Transaction(String transactionId, Member member, Book book) {
        this.transactionId = transactionId;
        this.member = member;
        this.book = book;
        this.transactionStatus = "Pending";
    }

    public String getTransaction(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        String borrowedDateString;
        if (borrowedDate != null) {
            borrowedDateString = borrowedDate.format(formatter);
        } else {
            borrowedDateString = "Not yet Borrowed";
        }
        
        String returnDateString;
        if (returnDate != null) {
            returnDateString = returnDate.format(formatter);
        } else {
            returnDateString = "Not yet Returned";
        }
        
        return "Transaction ID : " + this.transactionId + "\nBook Title : " + this.book.getTitle() + "\nISBN : " + this.book.getISBN() + "\nMember ID : " + this.member.getUserId() + "\nName : " + this.member.getName() + "\nBorrowed Date : " + borrowedDateString + "\nReturned Date : " + returnDateString + "\nTransaction Status : " + this.transactionStatus;
    }

    public void borrowBook() {
        this.borrowedDate = LocalDate.now();
        this.dueDate = this.borrowedDate.plusDays(DEFAULT_BORROW_DAYS);
        this.transactionStatus = "Borrowed";
        this.book.borrowed();
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.transactionStatus = "Returned";
        this.book.returned();
    }

    public long overdueDays(){
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

    public String getTransactionId() { return this.transactionId; }
    public Member getMember() { return this.member; }
    public Book getBook() { return book; }
    public String getTransactionStatus() { return transactionStatus; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void gatTransactionHistory(LocalDate borrowedDate, LocalDate dueDate, LocalDate returnDate, String status) {
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.transactionStatus = status;
    }
}