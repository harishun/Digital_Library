public class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected String role;  
    public User(String userId, String name, String email, String phoneNumber, String password, String role){
        this.userId = userId;
        this.role = role;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
    public String getUserId(){
        return this.userId;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPhoneNumber(){
        return this.phoneNumber;
    }
    public String getRole(){
        return this.role;
    }
    public String[] authenticate(String password){
        if(this.password.equals(password)){
            return new String[]{this.userId, this.role};
        }
        else{
            return null;
        }
    }
    public String getUserInfo(){
        return "User ID : " + this.userId + "\nName : " + this.name + "\nEmail : " + this.email + "\nPhone Number : " + this.phoneNumber;
    }
}
