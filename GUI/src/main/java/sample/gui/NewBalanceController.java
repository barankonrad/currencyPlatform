package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import sample.gui.data.UserSingleton;
import sample.gui.tools.APIConnection;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

public class NewBalanceController implements Initializable{
    @FXML
    private ChoiceBox<String> currencyChoiceBox;
    private List<String> currencyList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        currencyList = APIConnection.getCurrencyList();
        currencyChoiceBox.getItems().setAll(currencyList);
        currencyChoiceBox.setValue(currencyList.get(0));
    }

    public void addBalanceQuery(){
        try(Statement statement = DBConnection.getConnection().createStatement()){
            UserSingleton user = UserSingleton.getInstance();
            String chosenCurrency = currencyChoiceBox.getValue();
            String query = String.format("EXEC addNewBalance %d, '%s'", user.getId(), chosenCurrency);

            statement.execute(query);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
