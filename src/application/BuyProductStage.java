package application;

import dao.BasketDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.*;

import java.io.IOException;

public class BuyProductStage {

    private final long count = 1;
    TableView<Product> tableView = new TableView<Product>();
    ProductDAO productDAO = new ProductDAO();
    BasketDAO basketDAO = new BasketDAO();

    final ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getProducts());
    final ObservableList<Basket> baskets = FXCollections.observableArrayList(basketDAO.getBaskets());

    private Stage stage = new Stage();
    private User loginUser;

    public BuyProductStage() throws IOException {

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Buyable Product List");
        tableView.setEditable(false);

        TableColumn<Product, String> id = new TableColumn<Product, String>("ID");
        TableColumn<Product, String> name = new TableColumn<Product, String>("Name");
        TableColumn<Product, String> description = new TableColumn<Product, String>("Description");
        TableColumn<Product, String> price = new TableColumn<Product, String>("Price");

        id.setCellValueFactory(new PropertyValueFactory<Product, String>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        description.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
        price.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));

        tableView.getColumns().add(id);
        tableView.getColumns().add(name);
        tableView.getColumns().add(description);
        tableView.getColumns().add(price);

        tableView.setItems(products);

        addButtonToTable();

        tableView.setPrefWidth(600);
        tableView.setPrefHeight(400);
        VBox vBox = addMenu();

        Scene scene = new Scene(vBox);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private VBox addMenu() {

        // create a menu
        Menu m = new Menu("View");

        // create menuitems
        MenuItem m1 = new MenuItem("Factor");
        MenuItem m2 = new MenuItem("Basket");

        // add menu items to menu
        m.getItems().add(m1);
        m.getItems().add(m2);

        // label to display events
        Label l = new Label("\t\t\t\t"
                + "no menu item selected");

        // create events for menu items
        
        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println(e.getSource());
                
             // load basket table
                if ((e.getSource()).equals(m2)) { 
                    BasketStage basketStage = new BasketStage();
                    basketStage.setLoginUser(loginUser);
                    
                 // set current stage title
                    stage.setTitle(BasketStage.title); 
                    stage.setScene(basketStage.getScene());
                    stage.show();
                }
                
             // load factor table
                else if ((e.getSource()).equals(m1)) { 
                    FactorStage factorStage = new FactorStage(loginUser);
                    
                 // set current stage title
                    stage.setTitle(FactorStage.title); 
                    stage.setScene(factorStage.getScene());
                    stage.show();
                } 
                
             // load basket table
                else if ((e.getSource()).equals(m2)) { 
                    BasketStage basketStage = new BasketStage();
                    
                 // set current stage title
                    stage.setTitle(BasketStage.title); 
                    stage.setScene(basketStage.getScene());
                    stage.show();
                }
            }
        };

        // add event
        m1.setOnAction(event);
        m2.setOnAction(event);

        // create a menubar
        MenuBar mb = new MenuBar();

        // add menu to menubar
        mb.getMenus().add(m);

        Node node = tableView;
        Group group = new Group(node);

        // create a VBox
        VBox vb = new VBox(mb, group);

        return vb;
    }

    private void addButtonToTable() {

        TableColumn<model.Product, Void> colBtn = new TableColumn("Action");

        Callback<TableColumn<Product, Void>, TableCell<Product, Void>> cellFactory = new Callback<TableColumn<Product, Void>, TableCell<Product, Void>>() {
            @Override
            public TableCell<Product, Void> call(final TableColumn<Product, Void> param) {
                final TableCell<Product, Void> cell = new TableCell<Product, Void>() {
                	
                    private final Button btn = new Button("Add To Card");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Product product = tableView.getItems().get(getIndex());
                            
                            //check duplicate product in basket
                            Basket previousBasket = basketDAO.getBasketByUserIdAndProductId(loginUser.getId(), product.getId());
                            
                            // if product exist, count++
                            if(previousBasket != null){
                                long c = previousBasket.getCount();
                                previousBasket.setCount(++c);
                                basketDAO.updateBasket(previousBasket);
                            }
                            else {
                                basketDAO.createBasket(new Basket(product.getId(), product.getId(), loginUser.getId(), count));
                            }
                            System.out.println("selectedProduct: " + product);
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

        colBtn.setCellFactory(cellFactory);

        tableView.getColumns().add(colBtn);
    }

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

    public Stage getStage(){
        return stage;
    }

    public void setLoginUser(User currentUser) {

        this.loginUser = currentUser;

    }
}
