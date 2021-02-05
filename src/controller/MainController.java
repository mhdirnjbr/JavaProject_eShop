package controller;

import dao.CategoryDAO;
import dao.DiscountDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final ProductDAO productDAO = new ProductDAO();
    private static final CategoryDAO categoryDAO = new CategoryDAO();
    private static final DiscountDAO discountDAO = new DiscountDAO();

    @FXML
    private TextField idField;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField countField;

    @FXML
    private ChoiceBox colorField;

    @FXML
    private ChoiceBox categoryField;

    @FXML
    private ChoiceBox discountField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Product> TableView;
    
    @FXML
    private TableColumn<Product, Long> idColumn;
    
    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> descriptionColumn;

    @FXML
    private TableColumn<Product, Long> priceColumn;

    @FXML
    private TableColumn<Product, Long> countColumn;

    @FXML
    private TableColumn<Product, Long> colorColumn;

    @FXML
    private TableColumn<Product, Long> categoryIdColumn;

    @FXML
    private TableColumn<Product, Long> discountIdColumn;

    @FXML
    private TextField productName;

    @FXML
    private Button searchButton;

    @FXML
    private void insertButton() {

        /*
    	String query = "insert into products values("+idField.getText()+",'"+ nameField.getText()+"','"+ descriptionField.getText()+"',"+ priceField.getText()+","+ countField.getText()+")";
    	executeQuery(query);
        */

        Colors color = (Colors) colorField.getSelectionModel().getSelectedItem();
        Categories category = (Categories) categoryField.getSelectionModel().getSelectedItem();
        Discounts discount = (Discounts) discountField.getSelectionModel().getSelectedItem();

        Product product = new Product(Long.parseLong(idField.getText()), nameField.getText(), descriptionField.getText(),
                Long.parseLong(priceField.getText()), color.ordinal(), discount.ordinal() , Long.parseLong(countField.getText()),
                category.ordinal(), 0);

        productDAO.createProduct(product);

        showProductList();
    }

    @FXML
    private void updateButton() {
        //String query = "UPDATE products SET Title='"+ nameField.getText()+"',Author='"+ descriptionField.getText()+"',Year="+ priceField.getText()+",Pages="+ countField.getText()+" WHERE ID="+idField.getText()+"";
        //executeQuery(query);;

        Colors color = (Colors) colorField.getSelectionModel().getSelectedItem();
        Categories category = (Categories) categoryField.getSelectionModel().getSelectedItem();
        Discounts discount = (Discounts) discountField.getSelectionModel().getSelectedItem();

        Product product = new Product(Long.parseLong(idField.getText()), nameField.getText(), descriptionField.getText(),
                Long.parseLong(priceField.getText()), color.ordinal(), discount.ordinal() , Long.parseLong(countField.getText()),
                category.ordinal(), 0);

        productDAO.updateProduct(product);

        showProductList();
        showCategories();
        showColors();
    }

    @FXML
    private void deleteButton() {
    	//String query = "DELETE FROM products WHERE ID="+idField.getText()+"";
    	//executeQuery(query);

        productDAO.deleteProduct(Long.parseLong(idField.getText()));

        clearFields();
    	showProductList();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        countField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showColors();
        showCategories();
        showDiscounts();
        showProductList();
    }

    private void showCategories() {

        ObservableList<Categories> list = FXCollections.observableArrayList(Categories.values());
        categoryField.setItems(list);
        categoryField.setValue(list.get(0));

    }

    private void showDiscounts() {

        ObservableList<Discounts> list = FXCollections.observableArrayList(Discounts.values());
        discountField.setItems(list);
        discountField.setValue(list.get(0));
    }

    private void showColors() {

        ObservableList<Colors> list = FXCollections.observableArrayList(Colors.values());
        colorField.setItems(list);
        colorField.setValue(list.get(0));

    }

    public ObservableList<Product> getProductList(){

        ObservableList<Product> products = FXCollections.observableArrayList();
        products = productDAO.getProducts();
        return products;
    }

    public void showProductList() {

     	ObservableList<Product> list = getProductList();

    	idColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("id"));
    	nameColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
    	descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product,String>("description"));
    	priceColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("price"));
    	countColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("count"));
    	colorColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("colorId"));
    	categoryIdColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("categoryId"));
    	discountIdColumn.setCellValueFactory(new PropertyValueFactory<Product,Long>("discountId"));

    	TableView.setItems(list);

        ObservableList<Colors> colors = FXCollections.observableArrayList(Colors.values());
        ObservableList<Categories> categories = FXCollections.observableArrayList(Categories.values());
        ObservableList<Discounts> discounts = FXCollections.observableArrayList(Discounts.values());

        TableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Product product = TableView.getSelectionModel().getSelectedItem();
                idField.setText(String.valueOf(product.getId()));
                nameField.setText(product.getName());
                descriptionField.setText(product.getDescription());
                priceField.setText(String.valueOf(product.getPrice()));
                countField.setText(String.valueOf(product.getCount()));

                // select category of product in choice box
                int categoryId = (int) product.getCategoryId();
                categoryField.setValue(categories.get(categoryId));

                // select discount of product in choice box
                int discountId = (int) product.getDiscountId();
                discountField.setValue(discounts.get(discountId));

                // select color of product in choice box
                int colorId = (int) product.getColorId();
                colorField.setValue(colors.get(colorId));

            }
        });
    }
}