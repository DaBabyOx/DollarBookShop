package Project;

import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import jfxtras.scene.control.window.Window;

public class UserHome {
    private String email = userSession.getInstance().getEmail();
    private String username = DBConnect.getUsername(email);
    private TableView<books> booksTable;
    private StackPane sp;

    public UserHome(Stage primaryStage) throws SQLException {
        MenuBar mb = MenuBar.createMenuBar(primaryStage, "UserHome");
        Label welcomeLabel = new Label("Hello, " + username);
        welcomeLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        booksTable = createTable();
        booksTable.setPrefHeight(900);
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(booksTable, Priority.ALWAYS);

        Button addToCart = new Button("Add to Cart");
        addToCart.setMinHeight(75);
        addToCart.setMinWidth(300);
        addToCart.setStyle("-fx-background-color: #808080; -fx-text-fill: white");
        addToCart.setOnAction(e -> {
            books selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                try {
                    addtoCart(selectedBook.getName(), selectedBook.getGenre(), selectedBook.getPrice());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showWarning("You need to select one product");
            }
        });

        VBox productLayout = new VBox(5, welcomeLabel, booksTable);
        productLayout.setAlignment(Pos.CENTER_LEFT);
        productLayout.setPadding(new Insets(10));

        VBox formsandbutton = new VBox(5, productLayout, addToCart);
        formsandbutton.setAlignment(Pos.CENTER);
        formsandbutton.setPadding(new Insets(10));

        VBox finalLayout = new VBox(10, mb, formsandbutton);
        finalLayout.setAlignment(Pos.TOP_CENTER);
        try{
            ObservableList<books> books = DBConnect.getBooks();
            booksTable.setItems(books);
        }catch(SQLException e){
            e.printStackTrace();}

        sp = new StackPane();
        sp.getChildren().add(finalLayout);
        Scene scene = new Scene(sp, 1920, 1080);
        primaryStage.setTitle("Dollar Book Shop");
        primaryStage.setScene(scene);
        primaryStage.show();}

    private TableView<books> createTable() {
        TableView<books> table = new TableView<>();
        TableColumn<books, String> IdColumn = new TableColumn<>("Id");
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<books, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<books, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<books, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<books, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(IdColumn, nameColumn, genreColumn, stockColumn, priceColumn);

        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setPrefWidth(150);
        }
        return table;
    }

    private void addtoCart(String name, String genre, int price) throws SQLException {
        int stock = DBConnect.getStock(name);
        int curQuant = DBConnect.getCartQuantity(email, name);

        Window popUp = new Window("Add to Cart");
        popUp.getLeftIcons().clear();
        popUp.setMaxSize(701, 440);

        Label nameLabel = new Label("Name: " + name);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label genreLabel = new Label("Genre: " + genre);
        Label priceLabel = new Label("Price: " + price);

        Spinner<Integer> quantity = new Spinner<>(0, stock, curQuant);
        quantity.setPrefSize(300, 30);
        quantity.setEditable(true);

        Button add = new Button("Add");
        add.setPrefSize(60, 30);
        add.setOnAction(e -> {
            int quantiti = quantity.getValue();
            if(quantiti>0){
                try{
                    if(curQuant == 0){
                        DBConnect.addtoCart(email, name, quantiti);
                        showSuccess("Product added successfully!");}
                    else{
                        DBConnect.updateCart(email, name, quantiti);
                        showSuccess("Product updated successfully!");}
                    popUp.close();
                }catch(SQLException ex){
                    ex.printStackTrace();}}
            else{
                showWarning("Quantity must be more than 0");}});

        VBox layout = new VBox(5, nameLabel, genreLabel, priceLabel, quantity, add);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setPadding(new Insets(10));

        popUp.getContentPane().getChildren().add(layout);
        sp.getChildren().add(popUp);

        popUp.toFront();}

    private void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();}

    private void showSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();}
}
