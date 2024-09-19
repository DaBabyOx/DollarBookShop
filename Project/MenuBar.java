package Project;

import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;


public class MenuBar extends javafx.scene.control.MenuBar{
    public static MenuBar createMenuBar(Stage primaryStage, String page){
        MenuBar mb = new MenuBar();

        Menu pageMenu = new Menu("Action");

        if(page.equals(("UserHome")) || page.equals("Cart")){
            MenuItem home = new MenuItem("Home");
            MenuItem cart = new MenuItem("Cart");
            MenuItem logout = new MenuItem("Logout");

            home.setOnAction(e -> {
                try{
                    new UserHome(primaryStage);}
                catch (SQLException ex) {
                    throw new RuntimeException(ex);}});
            cart.setOnAction(e -> {
                try {
                    new Cart(primaryStage);}
                catch (SQLException ex) {
                    throw new RuntimeException(ex);}});
            logout.setOnAction(e -> new login(primaryStage));

            pageMenu.getItems().addAll(home, cart, logout);}
        else if(page.equals("AdminHome")){
            MenuItem logout = new MenuItem("Logout");

            logout.setOnAction(e -> new login(primaryStage));

            pageMenu.getItems().addAll(logout);}

        mb.getMenus().add(pageMenu);

        return mb;}}
