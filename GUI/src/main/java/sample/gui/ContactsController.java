package sample.gui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.gui.data.ContactItem;
import sample.gui.data.UserSingleton;
import sample.gui.tools.DBConnection;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ContactsController implements Initializable{
    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<ContactItem> contactsListView;
    private FilteredList<ContactItem> filteredList;
    private ObservableList<ContactItem> dataList;
    private UserSingleton user;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        user = UserSingleton.getInstance();
        loadContacts();

        contactsListView.setCellFactory(list -> {
            ListCell<ContactItem> cell = new ListCell<>();

            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(event -> {
                ContactItem selected = cell.getItem();
                dataList.remove(selected);
                removeContactQuery(selected);
            });

            ContextMenu menu = new ContextMenu(delete);

            cell.emptyProperty().addListener((observable, wasEmpty, isEmpty) -> {
                if(isEmpty)
                    cell.setContextMenu(null);
                else
                    cell.setContextMenu(menu);
            });

            cell.textProperty()
                .bind(Bindings.when(cell.emptyProperty()).then("")
                    .otherwise(cell.itemProperty().asString()));

            return cell;
        });

        searchTextField.textProperty().addListener(event -> {
            String filter = searchTextField.getText();
            if(filter.isEmpty())
                filteredList.setPredicate(predicate -> true);
            else
                filteredList.setPredicate(
                    predicate -> predicate.toString().toLowerCase().contains(filter.toLowerCase()));
        });
    }
    private void loadContacts(){
        user.loadContacts();
        dataList = FXCollections.observableArrayList(user.getContactsList());

        filteredList = new FilteredList<>(dataList, predicate -> true);
        contactsListView.setItems(filteredList);
        contactsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        contactsListView.getSelectionModel().selectFirst();
    }

    public void addContactQuery(){
        try(Connection conn = DBConnection.getConnection()){
            String login = searchTextField.getText();
            PreparedStatement ps = conn.prepareStatement("SELECT ClientID FROM Clients WHERE " +
                "Login = ?");

            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int friendId = rs.getInt("ClientID");
                CallableStatement cs = conn.prepareCall("{call addNewContact(?,?)}");
                cs.setInt(1, user.getId());
                cs.setInt(2, friendId);

                cs.execute();
                searchTextField.setText("");
                loadContacts();
            }
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void removeContactQuery(ContactItem item){
        try(Connection conn = DBConnection.getConnection()){
            CallableStatement cs = conn.prepareCall("{call deleteContact(?,?)}");
            cs.setInt(1, item.getUserId());
            cs.setInt(2, item.getFriendId());

            cs.execute();
            loadContacts();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
