package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import sample.gui.data.HashData;
import sample.gui.data.LoginData;
import sample.gui.tools.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
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

    private final List<LoginData> loginDataList = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try(Statement statement = DBConnection.getConnection().createStatement()){
            String query = "SELECT Login, Hash, Salt FROM Clients C JOIN Passwords P on C.ClientID = P.ClientID";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                String login = rs.getString("Login");
                String hash = rs.getString("Hash");
                int salt = Integer.parseInt(rs.getString("Salt"), 16);

                loginDataList.add(new LoginData(login, hash, salt));
            }
            System.out.println("Data loaded...");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void handleLoginButton(){
        String inputLogin = loginTextField.getText();
        String inputPassword = passwordTextField.getText();

        int index = loginDataList.indexOf(
                loginDataList.stream().filter(data -> data.getLogin().equals(inputLogin)).findAny().orElse(null));

        String inputHash = null;
        boolean loggedIn = false;

        if(index > -1){
            int salt = loginDataList.get(index).getSalt();
            inputHash = HashData.encodeHash(inputPassword, salt);
            loggedIn = inputHash.equals(loginDataList.get(index).getHash());
        }

        System.out.println(loggedIn);
    }

    public void handleSignInButton() throws IOException{
        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("registerView.fxml"));
        dialog.getDialogPane().setContent(fxmlLoader.load());
        dialog.setTitle("Sign up");
        dialog.setHeaderText("Fill the data below and press OK button to sign up");
        dialog.getDialogPane().getScene().getWindow()
                .setOnCloseRequest(event -> dialog.getDialogPane().getScene().getWindow().hide());

        RegisterController controller = fxmlLoader.getController();
        controller.setLoginDataList(loginDataList);
        dialog.showAndWait();
    }
}

/*
LOGIN: login
PASSWORD: password
SALT: 1601
 */