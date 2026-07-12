public class Administrator extends User {
    static private int adminNo;
    final private String adminId;
    Administrator(String name, String email, String phoneNumber, String password){
        this.adminId = "ADM-" + adminNo++;
        super(this.adminId, name, email, phoneNumber, password, "Administrator");
    }
}
