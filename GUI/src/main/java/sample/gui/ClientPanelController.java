package sample.gui;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.gui.data.AccountItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ClientPanelController implements Initializable{
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<AccountItem> accountListView;
    private FilteredList<AccountItem> filteredList;
    private SortedList<AccountItem> sortedList;
    private UserSingleton user;
    private ListView<String> contactsListView = new ListView<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        user = UserSingleton.getInstance();
        loadData();
        loadAccounts();
        welcomeLabel.setText(String.format("Welcome %s %s!", user.getFirstName(), user.getLastName()));

        // TODO: 10.03.2023 FILTERED AND SORTED LISTS OF ACCOUNTS LIST
    }

    public void handleNewBalanceButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("newBalanceView.fxml");

        dialog.setTitle("New account");
        dialog.setHeaderText("Select a currency for your account");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        NewBalanceController controller = dialog.getLoader().getController();

        dialog.showAndWait().filter(result -> result.equals(ButtonType.OK)).ifPresent(result -> {
            controller.addBalanceQuery();
            loadAccounts();
        });
    }

    public void handleContactsButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("contactsView.fxml");
        dialog.setTitle("Contact's list");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }

    public void handleRatesHistoryButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("ratesHistoryView.fxml");
        dialog.setTitle("Rates history");

        dialog.showAndWait();
    }

    private void loadData(){
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Clients C WHERE C.ClientID = ?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void loadAccounts(){
        LinkedList<AccountItem> list = new LinkedList<>();
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Balances WHERE ClientID = ?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("BalanceID");
                String currency = rs.getString("Currency");
                String name = rs.getString("Name");
                double balance = rs.getDouble("Balance");

                list.add(new AccountItem(id, currency, name, balance));
            }
            accountListView.getItems().setAll(list);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

}
