package controller;

import dao.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    private static final UserDAO userDAO = new UserDAO();
    private static AddressDAO addressDAO = new AddressDAO();
    private static RoleDAO roleDAO = new RoleDAO();

    @FXML
    private TextField idField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField budgetField;

    @FXML
    private TextField addressField;

    @FXML
    private ChoiceBox roleField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<User> TableView;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, Long> budgetColumn;

    @FXML
    private TableColumn<User, Long> addressColumn;

    @FXML
    private TableColumn<User, Long> roleColumn;

    @FXML
    private TableColumn<User, Long> phoneNumberColumn;


    @FXML
    private void insertButton() {

        /*
    	String query = "insert into users values("+idField.getText()+",'"+ nameField.getText()+"','"+ descriptionField.getText()+"',"+ priceField.getText()+","+ countField.getText()+")";
    	executeQuery(query);
        */

        Roles role = (Roles) roleField.getSelectionModel().getSelectedItem();
        Address address = addressDAO.createAddress(new Address(1, addressField.getText()));
        User user = new User(Long.parseLong(idField.getText()), usernameField.getText(), passwordField.getText(),
                BigDecimal.valueOf(Long.parseLong(budgetField.getText())), 0,0, address.getId(),
                role.ordinal(), phoneNumberField.getText());

        userDAO.createUser(user);

        showUserList();
    }

    @FXML
    private void updateButton() {
        //String query = "UPDATE users SET Title='"+ nameField.getText()+"',Author='"+ descriptionField.getText()+"',Year="+ priceField.getText()+",Pages="+ countField.getText()+" WHERE ID="+idField.getText()+"";
        //executeQuery(query);;

        Roles role = (Roles) roleField.getSelectionModel().getSelectedItem();
        Address address = addressDAO.createAddress(new Address(1, addressField.getText()));
        User user = new User(Long.parseLong(idField.getText()), usernameField.getText(), passwordField.getText(),
                BigDecimal.valueOf(Long.parseLong(budgetField.getText())), 0,0, address.getId(),
                role.ordinal(), phoneNumberField.getText());

        userDAO.updateUser(user);

        showUserList();
    }

    @FXML
    private void deleteButton() {
    	//String query = "DELETE FROM users WHERE ID="+idField.getText()+"";
    	//executeQuery(query);

        userDAO.deleteUser(Long.parseLong(idField.getText()));

        clearFields();
    	showUserList();
    }

    private void clearFields() {
        idField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        budgetField.setText("");
        phoneNumberField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showRoles();
        showUserList();
    }

    private void showRoles() {

        ObservableList<Roles> list = FXCollections.observableArrayList(Roles.values());
        roleField.setItems(list);
        roleField.setValue(list.get(0));

    }

    public ObservableList<User> getUserList(){

        ObservableList<User> users = FXCollections.observableArrayList();
        users = userDAO.getUsers();
        return users;
    }

    public void showUserList() {

     	ObservableList<User> list = getUserList();

    	idColumn.setCellValueFactory(new PropertyValueFactory<User,Long>("id"));
    	usernameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
    	passwordColumn.setCellValueFactory(new PropertyValueFactory<User,String>("password"));
    	budgetColumn.setCellValueFactory(new PropertyValueFactory<User,Long>("budget"));
    	addressColumn.setCellValueFactory(new PropertyValueFactory<User,Long>("addressId"));
    	roleColumn.setCellValueFactory(new PropertyValueFactory<User,Long>("roleId"));
    	phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<User,Long>("phoneNumber"));

    	TableView.setItems(list);

        ObservableList<Roles> roles = FXCollections.observableArrayList(Roles.values());
        TableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                User user = TableView.getSelectionModel().getSelectedItem();
                idField.setText(String.valueOf(user.getId()));
                usernameField.setText(user.getUsername());
                passwordField.setText(user.getPassword());
                budgetField.setText(String.valueOf(user.getBudget()));
                addressField.setText(String.valueOf(user.getAddressId())); // addressdao.getAddressName
                phoneNumberField.setText(String.valueOf(user.getPhoneNumber()));

                // select color of user in choice box
                int roleId = (int) user.getRoleId();
                roleField.setValue(roles.get(roleId));

            }
        });
    }
}