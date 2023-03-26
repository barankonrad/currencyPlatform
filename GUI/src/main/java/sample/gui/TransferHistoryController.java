package sample.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.gui.data.TransferItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TransferHistoryController implements Initializable{
    @FXML
    private Label transactionIdLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label baseLabel;
    @FXML
    private Label targetLabel;
    @FXML
    private Label receiverLabel;
    @FXML
    private Label rateLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private TableColumn<TransferItem, String> dateColumn;
    @FXML
    private TableColumn<TransferItem, String> titleColumn;
    @FXML
    private TableColumn<TransferItem, String> contactColumn;
    @FXML
    private TableColumn<TransferItem, Double> amountColumn;
    @FXML
    private TableColumn<TransferItem, String> currencyColumn;
    @FXML
    private TableView<TransferItem> tableView;
    @FXML
    private TextField searchTextField;
    private UserSingleton user;
    private ObservableList<TransferItem> dataList;
    private FilteredList<TransferItem> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        user = UserSingleton.getInstance();
        loadHistory();

        dateColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().date()));
        titleColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().title()));
        contactColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().contact()));
        amountColumn.setCellValueFactory(item -> {
            if(!item.getValue().inOrOut())
                return new SimpleDoubleProperty(-item.getValue().amountSent()).asObject();
            else
                return new SimpleDoubleProperty(item.getValue().amountSent()).asObject();
        });
        currencyColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().currency()));

        searchTextField.textProperty().addListener((event -> {
            String filter = searchTextField.getText().toLowerCase();
            filteredList.setPredicate(transfer -> {
                if(filter.isEmpty())
                    return true;
                else if(transfer.date().contains(filter))
                    return true;
                else if(transfer.title().toLowerCase().contains(filter))
                    return true;
                else if(transfer.contact().toLowerCase().contains(filter))
                    return true;
                else if(transfer.currency().toLowerCase().contains(filter))
                    return true;
                else if(transfer.inOrOut() && filter.equals("in") || !transfer.inOrOut() && filter.contains("out"))
                    return true;

                return false;
            });
        }));

        tableView.setOnMouseClicked(event -> updateDetails(tableView.getSelectionModel().getSelectedItem()));

        tableView.setItems(filteredList);
    }

    private void loadHistory(){
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM GetTransferHistory(?)");
            ps.setInt(1, user.getId());

            LinkedList<TransferItem> list = new LinkedList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
//                in = true   out = false
                boolean inOrOut = rs.getString("InOut").equals("IN");
                int transferID = rs.getInt("TransferID");
                String date = rs.getString("Date");
                String title = rs.getString("Title");
                String contact = rs.getString("Contact");
                double amountSent = rs.getDouble("Amount sent");
                String currencySent = rs.getString("CurrencySent");
                double amountExchanged = rs.getDouble("AmountExchanged");
                double exchangeRate = rs.getDouble("ExchangeRate");
                String currency = rs.getString("Currency");

                list.add(new TransferItem(inOrOut, transferID, date, title, contact, amountSent, currencySent,
                    amountExchanged, exchangeRate, currency));
            }

            dataList = FXCollections.observableArrayList(list);
            filteredList = new FilteredList<>(dataList, predicate -> true);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void updateDetails(TransferItem item){
        transactionIdLabel.setText(String.format("Transaction no %d", item.transferID()));
        titleLabel.setText(item.title());
        baseLabel.setText(item.amountSent() + " " + item.currencySent());
        targetLabel.setText(item.amountExchanged() + " " + item.currency());
        receiverLabel.setText(item.contact());
        rateLabel.setText(String.valueOf(item.exchangeRate()));
        dateLabel.setText(item.date());
    }
}
