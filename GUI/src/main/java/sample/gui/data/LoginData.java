package sample.gui.data;

public class LoginData{

    private final String login;
    private final String hash;
    private final int salt;

    public LoginData(String login, String hash, int salt){
        this.login = login;
        this.hash = hash;
        this.salt = salt;
    }

    public String getLogin(){
        return login;
    }

    public String getHash(){
        return hash;
    }

    public int getSalt(){
        return salt;
    }
}
