package sample.gui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import sample.gui.data.AccountItem;
import sample.gui.data.ContactItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.APIConnection;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransferController implements Initializable{
    @FXML
    private ChoiceBox<AccountItem> accountChoiceBox;
    @FXML
    private ChoiceBox<ContactItem> receiverChoiceBox;
    @FXML
    private ChoiceBox<AccountItem> receiverAccountChoiceBox;
    @FXML
    private Spinner<Double> amountSpinner;
    @FXML
    private TextField titleTextField;
    private UserSingleton user;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        user = UserSingleton.getInstance();

        user.loadAccounts();
        accountChoiceBox.setItems(FXCollections.observableArrayList(user.getAccountsList()));

        user.loadContacts();
        receiverChoiceBox.getSelectionModel().selectedItemProperty()
            .addListener(((observable, oldValue, newValue) -> {
                if(newValue != null){
                    receiverAccountChoiceBox.setItems(FXCollections.observableArrayList(getContactsAccounts(newValue)));
                    receiverAccountChoiceBox.setDisable(false);
                }
            }));
        receiverChoiceBox.setItems(FXCollections.observableArrayList(user.getContactsList()));

        receiverAccountChoiceBox.setDisable(true);
        receiverAccountChoiceBox.setConverter(new StringConverter<AccountItem>(){
            @Override
            public String toString(AccountItem object){
                if(object != null)
                    return object.getCurrency();
                return null;
            }

            @Override
            public AccountItem fromString(String string){
                return null;
            }
        });

        SpinnerValueFactory.DoubleSpinnerValueFactory valueFactory =
            new SpinnerValueFactory.DoubleSpinnerValueFactory(1, Double.MAX_VALUE, 1, 0.25);
        valueFactory.setConverter(new StringConverter<Double>(){
            @Override
            public String toString(Double value){
                return String.format("%.2f", value);
            }

            @Override
            public Double fromString(String string){
                return null;
            }
        });
        amountSpinner.setValueFactory(valueFactory);
    }

    private List<AccountItem> getContactsAccounts(ContactItem contact){
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Balances WHERE ClientID = ?");
            ps.setInt(1, contact.getFriendId());

            List<AccountItem> list = new LinkedList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("BalanceID");
                String currency = rs.getString("Currency");
                list.add(new AccountItem(id, currency));
            }

            return list;
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void makeTransferQuery(){
        try(Connection conn = DBConnection.getConnection()){
            CallableStatement cs = conn.prepareCall("{call addNewTransfer(?, ?, ?, ?, ?)}");
            AccountItem from = accountChoiceBox.getValue();
            AccountItem to = receiverAccountChoiceBox.getValue();
            double rate = APIConnection.getLatest(from.getCurrency(), to.getCurrency());

            cs.setInt(1, from.getId());
            cs.setInt(2, to.getId());
            cs.setDouble(3, amountSpinner.getValue());
            cs.setDouble(4, rate);
            cs.setString(5, titleTextField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("%s will receive %.3f %s. Do you want to perform this operation?",
                receiverChoiceBox.getValue().getFriendFullName(), (rate * amountSpinner.getValue()), to.getCurrency()));
            alert.getDialogPane().getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get().equals(ButtonType.YES))
                cs.execute();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
