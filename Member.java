class Member extends User {
    static private int memberNo = 1;
    
    // START: Implemented using Antigravity AI (Gemini)
    private double balance = 0.0;
    // END: Implemented using Antigravity AI (Gemini)
    
    Member(String name, String email, String phoneNumber, String password){
        super("MEM-" + memberNo++, name, email, phoneNumber, password, "Member");
    }
    
    public double getBalance() { return this.balance; }
    public static void setMemberNo(int nextNo) {
        memberNo = nextNo;
    }

    // START: Implemented using Antigravity AI (Gemini)
    public void addFine(double amount) { this.balance += amount; }
    public void payFineAmount(double amount) {
        this.balance = Math.max(0.0, this.balance - amount);
    }
    // END: Implemented using Antigravity AI (Gemini)
}