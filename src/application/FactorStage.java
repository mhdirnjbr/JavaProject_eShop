package application;

import dao.FactorDAO;
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
import model.Basket;
import model.Factor;
import model.User;

import java.util.List;

public class FactorStage {

    private static final long CUSTOMER = 1;
    private static final long ADMIN = 2;
    private DatePicker from;
    private DatePicker to;
    TableView<Factor> tableView = new TableView<Factor>();
    FactorDAO factorDAO = new FactorDAO();

    final ObservableList<Factor> allFactors = FXCollections.observableArrayList(factorDAO.getFactors());
    ObservableList<Factor> factors = FXCollections.observableArrayList();

    static Stage stage = new Stage();
    private User loginUser;
    private Basket basket;

    Button submitRequest = new Button("Search");

    // Add Sum Label
    Label totalSell = new Label();

    static String title = "Factor List";

    Scene scene;

    public FactorStage(User user) {

        Stage primaryStage = new Stage();

        primaryStage.setTitle(title);
        tableView.setEditable(false);

        TableColumn<Factor, String> factorId = new TableColumn<Factor, String>("ID");
        TableColumn<Factor, String> basketId = new TableColumn<Factor, String>("Basket ID");
        TableColumn<Factor, String> userId = new TableColumn<Factor, String>("User ID");
        TableColumn<Factor, String> price = new TableColumn<Factor, String>("Price");
        TableColumn<Factor, String> date = new TableColumn<Factor, String>("Date");
        TableColumn<Factor, String> delivery = new TableColumn<Factor, String>("Delivered");

        factorId.setCellValueFactory(new PropertyValueFactory<Factor, String>("id"));
        basketId.setCellValueFactory(new PropertyValueFactory<Factor, String>("basketId"));
        userId.setCellValueFactory(new PropertyValueFactory<Factor, String>("userId"));
        price.setCellValueFactory(new PropertyValueFactory<Factor, String>("price"));
        date.setCellValueFactory(new PropertyValueFactory<Factor, String>("date"));
        delivery.setCellValueFactory(new PropertyValueFactory<Factor, String>("delivery"));

        tableView.getColumns().add(factorId);
        tableView.getColumns().add(basketId);
        tableView.getColumns().add(userId);
        tableView.getColumns().add(price);
        tableView.getColumns().add(date);
        tableView.getColumns().add(delivery);

        totalSell = new Label("Total Sell : ");
        totalSell.styleProperty().set("-fx-font-weight: bold");
        
        if(user.getRoleId() == ADMIN) {
        	
        	// list of all customer's factors
            tableView.setItems(allFactors); 
            calculateTotalSell(allFactors);
        }
        else if(user.getRoleId() == CUSTOMER){
            factors = FXCollections.observableArrayList(factorDAO.getFactorsByUserId(user.getId()));
            
         // list of all factors of login customer
            tableView.setItems(factors); 
            calculateTotalSell(factors);
        }

        tableView.setPrefWidth(600);
        tableView.setPrefHeight(400);

        Label startDate = new Label("Start Date : ");
        Label endDate = new Label("End Date : ");
        from = new DatePicker();
        to = new DatePicker();
        Node node = tableView;
        Group group = new Group(node);
        
        
        // create a VBox
        VBox vb;
        if(user.getRoleId() == ADMIN){
            vb = new VBox(group, startDate, this.from, endDate, this.to, this.submitRequest, totalSell);
        }
        else{
            vb = new VBox(group);
        }

        scene = new Scene(vb, 750, 600);
        primaryStage.setScene(scene);

        submitRequest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(to);
                List<Factor> factorList = factorDAO.findByDate(from.getValue(), to.getValue());
                ObservableList<Factor> list = FXCollections.observableArrayList(factorList);
                totalSell = new Label("Total Sell : ");
                totalSell.styleProperty().set("-fx-font-weight: bold");
                calculateTotalSell(factorList);
                tableView.setItems(list);
                System.out.println(factorList.size());
            }
        });
    }

    private void calculateTotalSell(List<Factor> factors) {

        long total = 0;
        
        for(Factor factor : factors){
            total += factor.getPrice();
        }
        totalSell.setText(totalSell.getText() + total + " $");
    }

    Scene getScene() {
        return scene;
    }

    void setBasket(Basket basket){
        this.basket = basket;
    }

}
