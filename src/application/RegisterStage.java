package application;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.User;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

public class RegisterStage {

    private Stage primaryStage = new Stage();

    private static final int UNKNOWN = 0;

    private UserDAO userDAO = new UserDAO();

    public RegisterStage(){
    	
        // set instance variable to use outside of this class
        this.primaryStage = primaryStage;

        primaryStage.setTitle("Welcome To Comfort Commerce!");

        // Create the registration form pane
        GridPane gridPane = createRegistrationFormPane();

        // add ui elements to pane
        addUIControls(gridPane);

        // Create a scene with the registration form gridPane as the root node.
        Scene scene = new Scene(gridPane, 800, 500);
        
        // Set the scene in primary stage
        primaryStage.setScene(scene);
    }


    private GridPane createRegistrationFormPane() {
    	
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
        //if the screen resizes, then column 2 should grow horizontally and fill in the extra space.
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane) {
    	
        // Add Header
        Label headerLabel = new Label("Registration Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0, 1, 2, 1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0, 20, 0));

        // Add Email Label
        Label emailLabel = new Label("Username");
        gridPane.add(emailLabel, 0, 3);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 3);

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
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 4);

        // Add Budget Label
        Label budget = new Label("Budget : ");
        gridPane.add(budget, 0, 5);

        // Add Budget Field
        TextField budgetField = new TextField();
        budgetField.setPrefHeight(40);
        gridPane.add(budgetField, 1, 5);

        // Add PhoneNumber Label
        Label phoneNumber = new Label("Phone : ");
        gridPane.add(phoneNumber, 0, 6);

        // Add PhoneNumber Field
        TextField phoneNumberField = new TextField("+1");
        phoneNumberField.setPrefHeight(40);
        gridPane.add(phoneNumberField, 1, 6);

        // Add Submit Button
        Button submitButton = new Button("Register");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 7, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0, 20, 0));

    submitButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            boolean error = false;
            if (emailField.getText().isEmpty()) {
                showAlert("Form Error!", "Please enter a username");
                error = true;
            }
            if (passwordField.getText().isEmpty()) {
                showAlert("Form Error!", "Please enter a password");
                error = true;
            }
            if (phoneNumberField.getText().isEmpty()) {
                showAlert("Form Error!", "Please enter a phone number");
                error = true;
            }
            if(!budgetField.getText().matches("[0-9]+")){
                showAlert("Form Error!", "Please enter a number for user budget");
                error = true;
            }else if (budgetField.getText().isEmpty()) {
                showAlert("Form Error!", "Please enter user budget");
                error = true;
            }
            if (error == false) {

                User user = new User(0, emailField.getText(), passwordField.getText(), BigDecimal.valueOf(Double.parseDouble(budgetField.getText())),
                        0, 0, 0, UNKNOWN, phoneNumberField.getText());

                userDAO.createUser(user);
                showAlert("Registration Successful!", "please wait till the admin validate your registration, " + emailField.getText());

                }
            }
        });
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public Stage getStage() {
        return primaryStage;
    }

}
