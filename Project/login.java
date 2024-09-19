package Project;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class login {
    public login(Stage primaryStage) {
        Label loginLabel = new Label("Login");
        loginLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setMinHeight(30);
        emailField.setMinWidth(300);

        VBox emailLayout = new VBox(5, emailLabel, emailField);
        emailLayout.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMinHeight(30);
        passwordField.setMinWidth(300);

        VBox passwordLayout = new VBox(5, passwordLabel, passwordField);
        passwordLayout.setAlignment(Pos.CENTER_LEFT);

        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-background-color: #00CED1");
        loginButton.setMinHeight(30);
        loginButton.setMinWidth(300);
        loginButton.setOnAction(e->{
            if(emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
                showWarning("Please fill in all fields!");}

            if(!emailField.getText().contains("@")){
                showWarning("Invalid credentials!");}
            else{
                try{
                    if(DBConnect.validateLogin(emailField.getText(), passwordField.getText())){
                        String userRole = DBConnect.getURole(emailField.getText(), passwordField.getText());
                        userSession.getInstance().setEmail(emailField.getText());
                        if(userRole != null){
                            if(userRole.equals("admin")){
                                new AdminHome(primaryStage);}
                            else if(userRole.equals("user")){
                                new UserHome(primaryStage);}}
                        else{
                            showWarning("Invalid credentials!");}
                    }
                    else{
                        showWarning("Invalid credentials!");}
                }catch(SQLException ex){
                    ex.printStackTrace();}}});

        Hyperlink signUpLink = new Hyperlink("Don't have an account? Register here!");
        signUpLink.setVisited(false);
        signUpLink.setOnAction(e -> new register(primaryStage));

        VBox loginLayout = new VBox(10, emailLayout, passwordLayout, loginButton, signUpLink);
        loginLayout.setAlignment(Pos.CENTER);

        HBox center = new HBox(loginLayout);
        center.setAlignment(Pos.CENTER);

        VBox formLayout = new VBox(10,loginLabel, center);
        formLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(formLayout, 1920, 1080);
        primaryStage.setTitle("Dollar Book Shop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();}
}
