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

import java.net.URL;
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

        Consumer<TextField> highlightOn = textField -> {
            textField.setStyle("-fx-border-color: red");
        };

        Consumer<TextField> highlightOff = textField -> {
            textField.setStyle(null);
        };

        loginTextField.focusedProperty().addListener(observable -> {
            isLoginOccupied.set(loginDataList.stream().map(LoginDataItem::getLogin)
                    .anyMatch(login -> login.equals(loginTextField.getText())));
        });

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
        // =========

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
        if(!checkData()){
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Invalid data. Correct your details.");
        }
        else{
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("User " + loginTextField.getText() + " signed in successfully.");

            // TODO: 27.02.2023 ADD QUERY CREATING USER
            String query = "CREATE NEW USER";
        }
        alert.showAndWait();
        doneButton.getScene().getWindow().hide();
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
