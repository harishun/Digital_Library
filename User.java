public class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String password;
    protected String role;  
    public User(String userId, String name, String email, String phoneNumber, String password, String role){
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
    public String[] authenticate(String password){
        if(this.password.equals(password)){
            return new String[]{this.userId, this.role};
        }
        else{
            return null;
        }
    }
}
