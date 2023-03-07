package sample.gui.data;

import java.util.List;

public class UserSingleton{
    private int id;
    private String login;
    private String password;
    private List<AccountItem> list;
    private String firstName;
    private String lastName;

    private static final UserSingleton instance = new UserSingleton();

    private UserSingleton(){}

    public static UserSingleton getInstance(){
        return instance;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<AccountItem> getList(){
        return list;
    }

    public void setList(List<AccountItem> list){
        this.list = list;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
}
