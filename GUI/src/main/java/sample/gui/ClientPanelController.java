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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientPanelController implements Initializable{
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<AccountItem> accountListView;
    private List<AccountItem> accountsList;
    private FilteredList<AccountItem> filteredList;
    private SortedList<AccountItem> sortedList;
    private UserSingleton user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadData();
        welcomeLabel.setText(String.format("Welcome %s %s!", user.getFirstName(), user.getLastName()));

        // TODO: 10.03.2023 FILTERED AND SORTED LISTS OF ACCOUNTS LIST
        accountListView.getItems().addAll(accountsList);

    }

    public void handleNewBalanceButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("newBalanceView.fxml");

        dialog.setTitle("New account");
        dialog.setHeaderText("Select a currency for your account");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        NewBalanceController controller = dialog.getLoader().getController();

        dialog.showAndWait()
            .filter(result -> result.equals(ButtonType.OK))
            .ifPresent(result -> controller.addBalanceQuery());
    }

    public void loadData(){
        user = UserSingleton.getInstance();
        accountsList = new LinkedList<>();

        try(Statement statement = DBConnection.getConnection().createStatement()){
            String query = "SELECT * FROM Clients C WHERE C.ClientID = " + user.getId();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                user.setFirstName(rs.getString("FirstName"));
                user.setLastName(rs.getString("LastName"));
            }

            query = "SELECT * FROM Balances WHERE ClientID = " + user.getId();
            rs = statement.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("BalanceID");
                String currency = rs.getString("Currency");
                double balance = rs.getDouble("Balance");

                accountsList.add(new AccountItem(id, currency, balance));
            }

            // TODO: 10.03.2023 LIST OF CONTACTS
            query = "SELECT * FROM Contacts WHERE UserID = " + user.getId();
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
