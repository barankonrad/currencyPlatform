package sample.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.gui.data.LoginDataItem;
import sample.gui.data.LoginDataList;
import sample.gui.tools.DBConnection;
import sample.gui.tools.HashTool;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class RegisterController implements Initializable{
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField loginTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmPasswordTextField;
    @FXML
    private Button doneButton;
    @FXML
    private Button cancelButton;

    private static List<LoginDataItem> loginDataList;
    private final BooleanProperty isLoginOccupied = new SimpleBooleanProperty(false);
    private final BooleanProperty arePasswordsTheSame = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loginDataList = LoginDataList.getInstance();

        Consumer<TextField> highlightOn = textField -> textField.setStyle("-fx-border-color: red");

        Consumer<TextField> highlightOff = textField -> textField.setStyle(null);

        loginTextField.focusedProperty().addListener(observable -> isLoginOccupied.set(
                loginDataList.stream().map(LoginDataItem::getLogin)
                        .anyMatch(login -> login.equals(loginTextField.getText()))));

        isLoginOccupied.addListener((observableValue, oldValue, newValue) -> {
            if(newValue){
                highlightOn.accept(loginTextField);
            }
            else{
                highlightOff.accept(loginTextField);
            }
        });

        confirmPasswordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue)
                arePasswordsTheSame.set(confirmPasswordTextField.getText().equals(passwordTextField.getText()));
        });

        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue)
                arePasswordsTheSame.set(confirmPasswordTextField.getText().equals(passwordTextField.getText()));
        });

        arePasswordsTheSame.addListener(((observableValue, oldValue, newValue) -> {
            if(!newValue){
                highlightOn.accept(confirmPasswordTextField);
            }
            else{
                highlightOff.accept(confirmPasswordTextField);
            }
        }));

    }

    public void handleDoneButton(){
        Alert alert = new Alert(Alert.AlertType.NONE);
        if(checkData()){
            String login = loginTextField.getText();
            String password = passwordTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            int salt = HashTool.generateNewSalt();
            String hash = HashTool.encodeHash(password, salt);

            String query =
                    String.format("EXEC addNewClient '%s', '%s', %d, '%s', '%s'", login, hash, salt,
                            firstName, lastName);
            try(Statement statement = DBConnection.getConnection().createStatement()){
                statement.execute(query);
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("User " + loginTextField.getText() + " signed in successfully.");
            }
            catch(SQLException e){
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText(e.getMessage());
            }
            alert.showAndWait();
            doneButton.getScene().getWindow().hide();
        }
        else{
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Invalid data. Correct your details.");

            alert.showAndWait();
        }
    }

    public void handleCancelButton(){
        cancelButton.getScene().getWindow().hide();
    }

    private boolean checkData(){
        return !loginTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty() &&
                !firstNameTextField.getText().isEmpty() && !lastNameTextField.getText().isEmpty() &&
                !isLoginOccupied.get() && arePasswordsTheSame.get();
    }
}
