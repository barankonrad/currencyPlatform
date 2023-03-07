package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import sample.gui.data.LoginDataItem;
import sample.gui.data.LoginDataList;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;
import sample.gui.tools.HashTool;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    private Label incorrectLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signInButton;
    private static List<LoginDataItem> loginDataList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loginDataList = LoginDataList.getInstance();

        loginTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER))
                passwordTextField.requestFocus();
        });

        passwordTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER))
                handleLoginButton();
        });

        try(Statement statement = DBConnection.getConnection().createStatement()){
            String query = "SELECT C.ClientID 'Id', Login, Hash, Salt FROM Clients C JOIN Passwords P on C.ClientID =" +
                    " P.ClientID";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("Id");
                String login = rs.getString("Login");
                String hash = rs.getString("Hash");
                int salt = Integer.parseInt(rs.getString("Salt"), 16);

                loginDataList.add(new LoginDataItem(id, login, hash, salt));
            }
            System.out.println("Data loaded...");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void handleLoginButton(){
        boolean loggedIn = false;
        String inputLogin = loginTextField.getText();
        String inputPassword = passwordTextField.getText();

        LoginDataItem tryingToLog =
                loginDataList.stream().filter(data -> data.getLogin().equals(inputLogin)).findAny().orElse(null);

        if(tryingToLog != null){
            int salt = tryingToLog.getSalt();
            if(tryingToLog.getHash().equals(HashTool.encodeHash(inputPassword, salt))){
                UserSingleton user = UserSingleton.getInstance();
                user.setLogin(inputLogin);
                user.setPassword(inputPassword);
                user.setId(tryingToLog.getId());

                login();
                return;
            }
        }

        incorrectLabel.setVisible(true);
    }

    public void handleSignInButton(){
        Dialog<ButtonType> dialog = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("registerView.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return;
        }
        dialog.setTitle("Sign up");
        dialog.setHeaderText("Fill the data below and press OK button to sign up");
        dialog.getDialogPane().getScene().getWindow()
                .setOnCloseRequest(event -> dialog.getDialogPane().getScene().getWindow().hide());

        dialog.showAndWait();
    }

    private void login(){
        Dialog<ButtonType> clientPanel = new Dialog<>();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("clientPanelView.fxml"));
        try{
            clientPanel.getDialogPane().setContent(fxmlLoader.load());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return;
        }
        clientPanel.setTitle("Client panel");
        clientPanel.getDialogPane().getScene().getWindow()
                .setOnCloseRequest(event -> clientPanel.getDialogPane().getScene().getWindow().hide());

        clientPanel.showAndWait();
    }
}

/*
LOGIN: login
PASSWORD: password
SALT: 1601
 */