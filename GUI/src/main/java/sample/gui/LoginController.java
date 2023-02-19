package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signInButton;

    private Statement statement;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try{
            statement = DBConnection.getConnection().createStatement();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public void loginOnClick() throws SQLException{

    }
}