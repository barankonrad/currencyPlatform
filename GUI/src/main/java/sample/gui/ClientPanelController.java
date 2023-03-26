package sample.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.gui.data.AccountItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.*;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ClientPanelController implements Initializable{
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> sortChoiceBox;
    @FXML
    private ListView<AccountItem> accountListView;
    private FilteredList<AccountItem> filteredList;
    private SortedList<AccountItem> sortedList;
    private ObservableList<AccountItem> dataList;
    private UserSingleton user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        user = UserSingleton.getInstance();
        loadData();
        loadAccounts();
        welcomeLabel.setText(String.format("Welcome %s %s!", user.getFirstName(), user.getLastName()));

        searchTextField.textProperty().addListener(event -> {
            String filter = searchTextField.getText();
            if(filter.isEmpty())
                filteredList.setPredicate(predicate -> true);
            else
                filteredList.setPredicate(
                    predicate -> predicate.toString().toLowerCase().contains(filter.toLowerCase()));
        });

        sortChoiceBox.setOnAction(event -> {
            switch(sortChoiceBox.getValue()){
                case "Sort by currency" -> sortedList.setComparator(Comparator.comparing(AccountItem::getCurrency));
                case "Sort by balance" -> sortedList.setComparator(Comparator.comparing(AccountItem::getBalance));
                case "Sort by creation date" -> sortedList.setComparator(Comparator.comparing(AccountItem::getId));

                default -> sortedList.setComparator(Comparator.comparing(AccountItem::toString));
            }
        });

        accountListView.setCellFactory(list -> {
            ListCell<AccountItem> cell = new ListCell<>();

            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event -> {
                AccountItem selected = cell.getItem();
                dataList.remove(selected);
                removeAccountQuery(selected);
            });
            MenuItem details = new MenuItem("Details");
           details.setOnAction(event -> showAccountDetails(cell.getItem()));

            ContextMenu menu = new ContextMenu(details, delete);

            cell.emptyProperty().addListener(((observable, wasEmpty, isEmpty) -> {
                if(isEmpty)
                    cell.setContextMenu(null);
                else
                    cell.setContextMenu(menu);
            }));

            cell.textProperty()
                .bind(Bindings.when(cell.emptyProperty()).then("")
                    .otherwise(cell.itemProperty().asString()));

            return cell;
        });
    }

    public void handleNewBalanceButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("newBalanceView.fxml");

        dialog.setTitle("New account");
        dialog.setHeaderText("Select a currency for your account");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        NewBalanceController controller = dialog.getLoader().getController();

        dialog.showAndWait().filter(result -> result.equals(ButtonType.OK))
            .ifPresent(result -> {
                controller.addBalanceQuery();
                loadAccounts();
            });
    }

    public void handleTransferButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("transferView.fxml");

        dialog.setTitle("New transfer");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        TransferController controller = dialog.getLoader().getController();

        dialog.showAndWait().filter(result -> result.equals(ButtonType.OK))
            .ifPresent(result -> {
                controller.makeTransferQuery();
                loadAccounts();
            });
    }

    public void handleTransferHistoryButton(){
        CustomDialog<ButtonType> dialog = new CustomDialog<>("transferHistoryView.fxml");

        dialog.setTitle("Transfer history");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);

        dialog.showAndWait();
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
        user.loadAccounts();
        dataList = FXCollections.observableArrayList(user.getAccountsList());

        filteredList = new FilteredList<>(dataList, predicate -> true);
        sortedList = new SortedList<>(filteredList);
        accountListView.setItems(sortedList);
    }

    private void removeAccountQuery(AccountItem account){
        try(Connection conn = DBConnection.getConnection()){
            CallableStatement cs = conn.prepareCall("{call deleteBalance(?)}");
            cs.setInt(1, account.getId());

            cs.execute();
            loadAccounts();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void showAccountDetails(AccountItem item){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(item.getName() + " details");
        alert.setContentText(String.format("Name: %s\nCurrency: %s\nCurrent balance: %f",
            item.getName(), item.getCurrency(), item.getBalance()));

        alert.showAndWait();
    }
}
