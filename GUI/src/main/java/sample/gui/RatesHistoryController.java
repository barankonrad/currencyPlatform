package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.util.Pair;
import sample.gui.tools.APIConnection;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class RatesHistoryController implements Initializable{
    @FXML
    private ChoiceBox<String> baseChoiceBox;
    @FXML
    private ChoiceBox<String> targetChoiceBox;
    @FXML
    private ChoiceBox<String> spanChoiceBox;
    @FXML
    private LineChart<String, Number> currencyChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        List<String> currencyList = APIConnection.getCurrencyList();
        baseChoiceBox.getItems().setAll(currencyList);
        baseChoiceBox.setValue("EUR");
        targetChoiceBox.getItems().setAll(currencyList);
        targetChoiceBox.setValue("USD");
        spanChoiceBox.setValue("last week");

        baseChoiceBox.setOnAction(event -> handleChartUpdate());
        targetChoiceBox.setOnAction(event -> handleChartUpdate());
        spanChoiceBox.setOnAction(event -> handleChartUpdate());

        handleChartUpdate();
    }

    public void handleSwapButton(){
        String base = baseChoiceBox.getValue();
        String target = targetChoiceBox.getValue();

        baseChoiceBox.setValue(target);
        targetChoiceBox.setValue(base);
    }

    public void handleChartUpdate(){
        String base = baseChoiceBox.getValue();
        String target = targetChoiceBox.getValue();
        LocalDate from = switch(spanChoiceBox.getValue()){
            case "last week" -> LocalDate.now().minusWeeks(1);
            case "last month" -> LocalDate.now().minusMonths(1);
            case "last quarter" -> LocalDate.now().minusMonths(3);
            default -> LocalDate.now();
        };

//        System.out.printf("%s -> %s, from %s to %s%n", base, target, from, LocalDate.now());

        List<Pair<LocalDate, Double>> list =
            APIConnection.getHistorical(base, target, from, LocalDate.now().minusDays(1));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        double minRate = Integer.MAX_VALUE;
        double maxRate = Integer.MIN_VALUE;

        for(Pair<LocalDate, Double> pair : list){
            LocalDate date = pair.getKey();
            Double rate = pair.getValue();

            minRate = Math.min(minRate, rate);
            maxRate = Math.max(maxRate, rate);

            series.getData().add(new XYChart.Data<>(date.toString(), rate));
        }


        currencyChart.getData().clear();
        xAxis.setLabel("Date");
        yAxis.setLabel(String.format("%s -> %s value", base, target));
        yAxis.setLowerBound(minRate - 0.01);
        yAxis.setUpperBound(maxRate + 0.01);
        yAxis.setTickUnit(0.01);
        currencyChart.getData().add(series);
    }
}
