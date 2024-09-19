package Project;

public class userSession {
    private static userSession instance;
    private String email;

    public static userSession getInstance(){
        if(instance == null){
            instance = new userSession();}
        return instance;}

    public void setEmail(String email){
        this.email = email;}

    public String getEmail(){
        return email;}}
