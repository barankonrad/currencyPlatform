package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import sample.gui.tools.APIConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class NewBalanceController implements Initializable{
    @FXML
    private ChoiceBox<String> currencyChoiceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadApiData();
    }

    private void loadApiData(){
        // TODO: 13.03.2023 API CONNECTION DOWNLOAD SYMBOLS
        APIConnection t = new APIConnection();
        t.getCurrencies();
    }
}
