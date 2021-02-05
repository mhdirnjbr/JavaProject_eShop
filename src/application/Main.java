package application;

import dao.UserDAO;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    private Scene scene;
    private Stage primaryStage;
    private UserDAO userDao = new UserDAO();
    private final static long ADMIN = 2;
    private static final long CUSTOMER = 1;
    private static final long UNKNOWN = 0;

    @Override
    public void start(Stage stage) throws Exception {

        this.primaryStage = stage;
        stage.setTitle("Login To Comfort Commerce!");
        
        //create GridPane with the method already defined
        GridPane gridPane = createLoginFormPane();

        // add ui elements to pane with the method already defined
        addUIControls(gridPane);
        scene = new Scene(gridPane, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createLoginFormPane() {
    	
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        
        // if the screen resizes, then column 2 should grow horizontally and fill in the extra space.
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {

        // Add Header
        Label headerLabel = new Label("Login Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 1, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Username Label
        Label emailLabel = new Label("Username : ");
        gridPane.add(emailLabel, 0, 3);

        // Add Username Text Field
        TextField usernameField = new TextField();
        usernameField.setPrefHeight(40);
        gridPane.add(usernameField, 1, 3);

        //Passing FileInputStream object as a parameter
        FileInputStream inputstream = null;
        try {
            inputstream = new FileInputStream("logo.JPEG");
        } catch (FileNotFoundException e) {
            System.err.println("Logo not found!" + e.getMessage());
        }

        //Creating an image
        Image image = new Image(inputstream);
        ImageView imageView = new ImageView(image);

        //setting the fit height and width of the image view
        imageView.setFitHeight(90);
        imageView.setFitWidth(90);

        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);
        gridPane.add(imageView, 0, 0, 2, 1);
        GridPane.setHalignment(imageView, HPos.CENTER);

        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 4);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setText("");
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 4);

        // Add Submit Button
        Button submitButton = new Button("Login");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

        // Add register Button
        Button registerButton = new Button("Register");
        registerButton.setPrefHeight(40);
        registerButton.setDefaultButton(true);
        registerButton.setPrefWidth(100);
        gridPane.add(registerButton, 0, 6, 2, 1);
        GridPane.setHalignment(registerButton, HPos.CENTER);
        GridPane.setMargin(registerButton, new Insets(20, 0, 20, 0));

        submitButton.setOnAction(event -> {
            Stage stage;
            Stage stage1;
            try {
                User currentUser = userDao.findUserByUsername(usernameField.getText());
                if(currentUser !=  null && currentUser.getPassword().equals(passwordField.getText())) {
                	
                	// check if user role is admin
                    if(currentUser.getRoleId() == ADMIN) { 
                        showAlert( "Login Successful!", "Welcome Administrator!");
                        ProductStage productStage = new ProductStage(currentUser);
                        
                        //productStage.setLoginUser(currentUser);
                        stage = productStage.getStage();
                        Scene scene = stage.getScene();
                        
                        // set current stage title
                        primaryStage.setTitle(stage.getTitle());
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    } 
                    
                    // check if user is already customer
                    else if(currentUser.getRoleId() == CUSTOMER){
                        showAlert( "Login Successful!", "Welcome " + currentUser.getUsername());
                        
                        // load to buy product page
                        BuyProductStage buyProductStage = new BuyProductStage();
                        buyProductStage.setLoginUser(currentUser);
                        stage1 = buyProductStage.getStage();
                        Scene scene = stage1.getScene();
                        
                        // set current stage title
                        primaryStage.setTitle(stage1.getTitle()); 
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    }
                    
                    // check if user is not customer yet
                    else if(currentUser.getRoleId() == UNKNOWN) {
                    	 showAlert( "request being processing", "your request is under process, please wait thanks" + currentUser.getUsername());
                    }
                }else{
                    showAlert("error!", "Incorrect Username and Password!");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });

        registerButton.setOnAction(event -> {
            Stage stage;
            stage = new RegisterStage().getStage();
            Scene scene = stage.getScene();
            primaryStage.setTitle(stage.getTitle());
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
