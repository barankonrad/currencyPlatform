package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.gui.data.UserSingleton;
import sample.gui.tools.APIConnection;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NewBalanceController implements Initializable{
    @FXML
    private ChoiceBox<String> currencyChoiceBox;
    @FXML
    private TextField nameTextField;
    private List<String> currencyList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        currencyList = APIConnection.getCurrencyList();
        currencyChoiceBox.getItems().setAll(currencyList);
        currencyChoiceBox.setValue(currencyList.get(0));
    }

    public void addBalanceQuery(){
        try(Connection conn = DBConnection.getConnection()){
            UserSingleton user = UserSingleton.getInstance();
            String chosenCurrency = currencyChoiceBox.getValue();
            String name = nameTextField.getText().trim();
            CallableStatement cs = conn.prepareCall("{call addNewBalance(?, ?, ?)}");
            cs.setInt(1,user.getId());
            cs.setString(2, chosenCurrency);
            cs.setString(3, name);

            cs.execute();
        }
        catch(SQLException e){
            System.out.println(e);
        }
    }
}
