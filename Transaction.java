import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Transaction {
    private String transactionId;
    private Member member;
    private Book book;
    private LocalDate borrowedDate;
    private LocalDate estimatedReturnDate;
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
        //Implemented Using AI (Gemini Flash-Lite)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String borrowedDateString = (borrowedDate != null) ? borrowedDate.format(formatter) : "Not yet Borrowed";
        String returnDateString = (returnDate != null) ? returnDate.format(formatter) : "Not yet Returned";
        //Manual
        return "Transaction ID : " + this.transactionId + "\nBook Title : " + this.book.getTitle() + "\nISBN : " + this.book.getISBN() + "\nMember ID : " + this.member.getUserId() + "\nName : " + this.member.getName() + "\nBorrowed Date : " + borrowedDateString + "\nReturned Date : " + returnDateString + "\nTransaction Status : " + this.transactionStatus;
    }

    public void borrowBook() {
        this.borrowedDate = LocalDate.now();
        this.estimatedReturnDate = this.borrowedDate.plusDays(DEFAULT_BORROW_DAYS);
        this.transactionStatus = "Borrowed";
        this.book.borrowed();
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.transactionStatus = "Returned";
        this.book.returned();
    }

    
    public long overdueDays(){
        LocalDate dateToCompare = (returnDate != null) ? returnDate : LocalDate.now();
        if (dateToCompare.isAfter(estimatedReturnDate)) {
            return ChronoUnit.DAYS.between(estimatedReturnDate, dateToCompare);
        }
        return 0;
    }

   
    public String getTransactionId() { return this.transactionId; }
    public Member getMember() { return this.member; }
    public Book getBook() { return book; }
    public String getTransactionStatus() { return transactionStatus; }
    public LocalDate getBorrowedDate() { return borrowedDate; }
    public LocalDate getEstimatedReturnDate() { return estimatedReturnDate; }
    public LocalDate getReturnDate() { return returnDate; }

     // START: Implemented using Antigravity AI (Gemini)
    public void loadTransactionState(LocalDate borrowedDate, LocalDate estimatedReturnDate, LocalDate returnDate, String status) {
        this.borrowedDate = borrowedDate;
        this.estimatedReturnDate = estimatedReturnDate;
        this.returnDate = returnDate;
        this.transactionStatus = status;
    }
    // END: Implemented using Antigravity AI (Gemini)
}