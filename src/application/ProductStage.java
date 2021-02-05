package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import model.User;

import java.io.IOException;

public class ProductStage {

    private Stage stage = new Stage();
    private  User loginUser;
    TableView<Product> tableView = new TableView<Product>();

    static String title = "Product Management";

    public ProductStage(User loginUser) throws IOException {
        this.loginUser = loginUser;
        Parent parent = (Parent) FXMLLoader.load(getClass().getResource(
                "/view/MainPane.fxml"));

        VBox vBox = addMenu(parent);

        Scene scene = new Scene(vBox, 800, 400);

        stage.setScene(scene);
        stage.setTitle("Welcome To Comfort Commerce!");

    }

    private VBox addMenu(Parent parent) {

        // create a menu
        Menu m = new Menu("View");

        // create menuitems
        MenuItem m1 = new MenuItem("Factor");
        MenuItem m2 = new MenuItem("User");

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
                
             // load factor table
                if ((e.getSource()).equals(m1)) { 
                    FactorStage factorStage = new FactorStage(loginUser);
                    
                 // set current stage title
                    stage.setTitle(FactorStage.title); 
                    stage.setScene(factorStage.getScene());
                    stage.show();
                }
                
             // load user table
                else if((e.getSource()).equals(m2)) { 
                    UserStage userStage = null;
                    try {
                        userStage = new UserStage();
                        
                     // set current stage title
                        stage.setTitle(userStage.title); 
                        stage.setScene(userStage.getStage().getScene());
                        stage.show();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
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

        Node node = parent;
        Group group = new Group(node);

        // create a VBox
        VBox vb = new VBox(mb, group);

        return vb;
    }

    public Stage getStage(){
        return stage;
    }

}
