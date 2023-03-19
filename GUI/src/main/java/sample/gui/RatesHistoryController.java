package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import sample.gui.tools.APIConnection;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RatesHistoryController implements Initializable{
    @FXML
    private ChoiceBox<String> baseChoiceBox;
    @FXML
    private ChoiceBox<String> targetChoiceBox;
    @FXML
    private ChoiceBox<String> spanChoiceBox;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        List<String> currencyList = APIConnection.getCurrencyList();
        baseChoiceBox.getItems().setAll(currencyList);
        baseChoiceBox.setValue("EUR");
        targetChoiceBox.getItems().setAll(currencyList);
        targetChoiceBox.setValue("USD");
        spanChoiceBox.setValue("last week");

        handleChartUpdate();
    }

    public void handleChartUpdate(){
        System.out.println("update!!!");
    }
}
