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

            cell.textProperty()
                .bind(Bindings.when(cell.emptyProperty()).then("")
                    .otherwise(cell.itemProperty().asString()));

            cell.emptyProperty().addListener((observable, wasEmpty, isEmpty) -> {
                if(isEmpty)
                    cell.setContextMenu(null);
                else
                    cell.setContextMenu(menu);
            });

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
        user = UserSingleton.getInstance();
        dataList = FXCollections.observableArrayList();
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM Contacts C JOIN Clients C2 ON C.FriendID = C2.ClientID "
                    + "WHERE C.UserID = ?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("ClientID");
                String login = rs.getString("Login");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");

                dataList.add(new ContactItem(user.getId(), id, login, firstName, lastName));
            }

            filteredList = new FilteredList<>(dataList, predicate -> true);
            contactsListView.setItems(filteredList);
            contactsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            contactsListView.getSelectionModel().selectFirst();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
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
            System.out.println(e.getMessage());
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
