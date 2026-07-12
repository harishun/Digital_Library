class Member extends User {
    static private int memberNo = 1;
    Member(String name, String email, String phoneNumber, String password){
        super("MEM-" + memberNo++, name, email, phoneNumber, password, "Member");
    }
}