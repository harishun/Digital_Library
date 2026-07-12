class Transaction {
    private String transactionId;
    private Member member;
    private Book book;
    private String transactionType; // "borrow" or "return"
    private String transactionDate;
    Transaction(String transactionId, Member member, Book book, String transactionType, String transactionDate) {
        this.transactionId = transactionId;
        this.member = member;
        this.book = book;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }
    public String getTransactionId() {
        return this.transactionId;
    }
    public String getLog() {
        return "Transaction ID: " + this.transactionId + ", Member ID: " + this.member.getUserId() + ", Book ISBN: " + this.book.getISBN() + ", Type: " + this.transactionType + ", Date: " + this.transactionDate;
    }
}