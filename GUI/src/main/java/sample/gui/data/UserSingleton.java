package sample.gui.data;

import sample.gui.tools.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserSingleton{
    private int id;
    private String login;
    private String password;
    private List<AccountItem> accountsList;
    private List<ContactItem> contactsList;
    private String firstName;
    private String lastName;

    private static final UserSingleton instance = new UserSingleton();

    private UserSingleton(){}

    public static UserSingleton getInstance(){
        return instance;
    }

    public void loadAccounts(){
        LinkedList<AccountItem> list = new LinkedList<>();
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Balances WHERE ClientID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("BalanceID");
                String currency = rs.getString("Currency");
                String name = rs.getString("Name");
                double balance = rs.getDouble("Balance");

                list.add(new AccountItem(id, currency, name, balance));
            }
            accountsList = list;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void loadContacts(){
        try(Connection conn = DBConnection.getConnection()){
            LinkedList<ContactItem> list = new LinkedList<>();
            PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM Contacts C JOIN Clients C2 ON C.FriendID = C2.ClientID "
                    + "WHERE C.UserID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("ClientID");
                String login = rs.getString("Login");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");

                list.add(new ContactItem(this.id, id, login, firstName, lastName));
            }

            contactsList = list;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
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

    public List<AccountItem> getAccountsList(){
        return accountsList;
    }

    public void setAccountsList(List<AccountItem> accountsList){
        this.accountsList = accountsList;
    }

    public List<ContactItem> getContactsList(){
        return contactsList;
    }

    public void setContactsList(List<ContactItem> contactsList){
        this.contactsList = contactsList;
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
