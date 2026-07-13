public class Administrator extends User {
    static private int adminNo = 1;
    Administrator(String name, String email, String phoneNumber, String password){
        super("ADM-" + adminNo++, name, email, phoneNumber, password, "Administrator");
    }
    public static void setAdminNo(int nextNo) {
        adminNo = nextNo;
    }
}
