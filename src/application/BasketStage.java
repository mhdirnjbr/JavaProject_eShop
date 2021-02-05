package application;

import dao.BasketDAO;
import dao.FactorDAO;
import dao.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Basket;
import model.Factor;
import model.Product;
import model.User;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasketStage {

    TableView<Basket> tableView = new TableView<Basket>();
    ProductDAO productDAO = new ProductDAO();
    BasketDAO basketDAO = new BasketDAO();

    final ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getProducts());
    ObservableList<Basket> baskets = FXCollections.observableArrayList(basketDAO.getBaskets());

    static Stage stage = new Stage();
    private User loginUser;

    // Add Sum Label
    Label totalPrice = new Label();

    Map<Long, Long> priceMap = new HashMap<>();
    Button submitRequest = new Button("Finish!");

    static String title = "User Basket List";
    static final int NOT_DELIVERED = 0;
    Scene scene;
    private FactorDAO factorDAO = new FactorDAO();

    public BasketStage() {

        Stage primaryStage = new Stage();

        primaryStage.setTitle(title);
        tableView.setEditable(false);

        TableColumn<Basket, String> productId = new TableColumn<Basket, String>("Product Id");
        TableColumn<Basket, String> userId = new TableColumn<Basket, String>("User Id");
        TableColumn<Basket, String> count = new TableColumn<Basket, String>("Count");

        productId.setCellValueFactory(new PropertyValueFactory<Basket, String>("productId"));
        userId.setCellValueFactory(new PropertyValueFactory<Basket, String>("userId"));
        count.setCellValueFactory(new PropertyValueFactory<Basket, String>("count"));

        tableView.getColumns().add(productId);
        tableView.getColumns().add(userId);
        tableView.getColumns().add(count);

        tableView.setItems(baskets);
        tableView.setPrefWidth(600);
        tableView.setPrefHeight(400);

        addButtonToTable();

        totalPrice.styleProperty().set("-fx-font-weight: bold");
        calculateTotalPrice();

        Node node = tableView;
        Group group = new Group(node);
        
        // create a VBox
        VBox vb = new VBox(group, this.totalPrice, this.submitRequest);
        
        handleSubmitRequest();

        scene = new Scene(vb);
        primaryStage.setScene(scene);
    }

    private void handleSubmitRequest() {

        submitRequest.setOnAction(x->{
            Date date = new Date(new java.util.Date().getTime());
            for (int i = 0; i < tableView.getItems().size(); i++) {
                Basket basket = tableView.getItems().get(i);
                Long price = priceMap.get(basket.getProductId());
                factorDAO.createFactor(new Factor(1, basket.getId(), basket.getUserId(), price, date, NOT_DELIVERED));
                basketDAO.deleteBasket(basket.getId());
                System.out.println("Factor Created!");
            }
            FactorStage factorStage = new FactorStage(loginUser);
            
            // set current stage title
            stage.setTitle(FactorStage.title);
            stage.setScene(factorStage.getScene());
            stage.show();
            tableView.setItems(null);
        });

    }

    // an observable calculator of total product prices
    private long calculateTotalPrice() {

        baskets = FXCollections.observableArrayList(basketDAO.getBaskets());
        List<Long> productIds = baskets.stream().map(Basket::getProductId).collect(Collectors.toList());
        long totalPrice = 0;
        for(Basket basket : baskets) {
            Product product = productDAO.getProduct(basket.getProductId());
            long count = basket.getCount();
            totalPrice += (count * product.getPrice());
            priceMap.put(product.getId(), totalPrice);
        }

        this.totalPrice.setText("Total Price : " + totalPrice + "$");

        return totalPrice;
    }

    private void addButtonToTable() {

        TableColumn<Basket, Void> colBtn = new TableColumn("Action");

        Callback<TableColumn<Basket, Void>, TableCell<Basket, Void>> cellFactory = new Callback<TableColumn<Basket, Void>, TableCell<Basket, Void>>() {
            @Override
            public TableCell<Basket, Void> call(final TableColumn<Basket, Void> param) {
                final TableCell<Basket, Void> cell = new TableCell<Basket, Void>() {
                    private final Button btn = new Button("Remove");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Basket basket = tableView.getItems().get(getIndex());
                            basketDAO.deleteBasket(basket.getId());
                            ObservableList<Basket> baskets = FXCollections.observableArrayList(basketDAO.getBaskets());
                            tableView.setItems(baskets);
                            calculateTotalPrice();
                            System.out.println("deleted basket: " + basket);
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

    Scene getScene(){
        return scene;
    }

    public void setLoginUser(User currentUser) {

        this.loginUser = currentUser;

    }

}
