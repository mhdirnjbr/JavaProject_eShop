package controller;

import dao.BasketDAO;
import dao.ProductDAO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Basket;
import model.Categories;
import model.Colors;
import model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class BuyProductController implements Initializable {

    TableView<Product> tableView = new TableView<Product>();
    ProductDAO productDAO = new ProductDAO();
    int count = 0;
    final ObservableList<Product> Product = FXCollections.observableArrayList(productDAO.getProducts());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Table With Delete Row");
        tableView.setEditable(false);

        TableColumn<Product, String> name = new TableColumn<Product, String>("Name");
        TableColumn<Product, String> description = new TableColumn<Product, String>("Description");
        TableColumn<Product, String> price = new TableColumn<Product, String>("Price");

        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        description.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        price.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));

        tableView.getColumns().add(name);
        tableView.getColumns().add(description);
        tableView.getColumns().add(price);

        TableColumn col_action = new TableColumn<>("Action");

        col_action.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Boolean>, ObservableValue<Boolean>>(){
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Product, Boolean> p){
                return new SimpleBooleanProperty(p.getValue() != null);
            }
        });

        col_action.setCellFactory(new Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>>(){
            @Override
            public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> p){
                return new ButtonCell(tableView);

            }
        });

        //Insert Button
        tableView.getColumns().add(col_action);

        Scene scene = new Scene(tableView);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void addButtonToTable() {

        TableColumn<model.Product, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {

                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product Product = getTableView().getItems().get(getIndex());
                            System.out.println("selectedProduct: " + Product);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
    }

    ;

    private void addToCardButton() {

        ProductDAO productDAO = new ProductDAO();
        BasketDAO basketDAO = new BasketDAO();
        Product product = productDAO.getProduct(1);
        Basket basket = basketDAO.createBasket(new Basket(product.getId(), product.getId(), 0, 0));
        System.out.println("Basket Created Successfully!");
    }


    public ObservableList<Product> getProductList() {

        ObservableList<Product> products = FXCollections.observableArrayList();
        products = productDAO.getProducts();
        return products;
    }

    public void showProductList() {

        ObservableList<Product> list = getProductList();

        tableView.setItems(list);

        ObservableList<Colors> colors = FXCollections.observableArrayList(Colors.values());
        ObservableList<Categories> categories = FXCollections.observableArrayList(Categories.values());

    }

    // Define the button cell
    private class ButtonCell extends TableCell<Product, Boolean> {
        final Button cellButton = new Button("Add");

        ButtonCell(final TableView<Product> tblView) {
            cellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        //Second s = new Second();
                        //s.start(new Stage());
                    } catch (Exception ex) {
                        //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }
}
