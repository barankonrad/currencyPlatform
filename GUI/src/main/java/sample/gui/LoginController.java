package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import sample.gui.data.LoginData;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;
import sample.gui.tools.HashTool;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML
    private Label incorrectLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField passwordTextField;
    private static List<LoginData.LoginDataItem> loginDataList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loginDataList = LoginData.getListInstance();
        loadLoginData();

        loginTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER))
                passwordTextField.requestFocus();
        });

        passwordTextField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER))
                handleLoginButton();
        });

    }

    public void handleLoginButton(){
        String inputLogin = loginTextField.getText();
        String inputPassword = passwordTextField.getText();

        LoginData.LoginDataItem tryingToLog =
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
        Dialog<ButtonType> dialog = new CustomDialog<>("registerView.fxml");
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("registerView.fxml"));
//        try{
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//        }
//        catch(IOException e){
//            System.out.println(e.getMessage());
//            return;
//        }
        dialog.setTitle("Sign up");
        dialog.setHeaderText("Fill the data below and press OK button to sign up");
//        dialog.getDialogPane().getScene().getWindow()
//                .setOnCloseRequest(event -> dialog.getDialogPane().getScene().getWindow().hide());

        dialog.showAndWait();
        loadLoginData();
    }

    private void login(){
        Dialog<ButtonType> clientPanel = new CustomDialog<>("clientPanelView.fxml");
        clientPanel.setTitle("Client panel");

        clientPanel.showAndWait();
    }

    private static void loadLoginData(){
        loginDataList.clear();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(
                "SELECT C.ClientID 'Id', Login, Hash, Salt FROM Clients C JOIN Passwords P on C.ClientID = P.ClientID");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("Id");
                String login = rs.getString("Login");
                String hash = rs.getString("Hash");
                int salt = Integer.parseInt(rs.getString("Salt"), 16);

                loginDataList.add(new LoginData.LoginDataItem(id, login, hash, salt));
            }
            System.out.println("Data loaded...");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}

/*
LOGIN: a
PASSWORD: a
 */