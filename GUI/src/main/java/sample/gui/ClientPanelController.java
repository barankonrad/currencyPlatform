package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.gui.data.AccountItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ClientPanelController implements Initializable{
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<AccountItem> accountListView;
    private UserSingleton user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadData();

        System.out.println(user.getId());
        System.out.println(user.getLogin());
        System.out.println(user.getPassword());

        welcomeLabel.setText("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
    }

    public void loadData(){
        user = UserSingleton.getInstance();

        try(Statement statement = DBConnection.getConnection().createStatement()){
            String query = "SELECT * FROM Clients C WHERE C.ClientID = " + user.getId();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
