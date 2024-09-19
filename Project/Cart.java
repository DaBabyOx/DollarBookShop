package Project;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;


public class Cart {
    private String email = userSession.getInstance().getEmail();
    private String username = DBConnect.getUsername(email);
    private TableView<carts> booksTable;


    public Cart(Stage primaryStage) throws SQLException {
        MenuBar mb = MenuBar.createMenuBar(primaryStage, "Cart");
        Label cartOwner = new Label(username + "'s Cart");
        cartOwner.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        booksTable = createTable();
        booksTable.setPrefHeight(900);
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(booksTable, Priority.ALWAYS);

        Spinner<Integer> quantity = new Spinner<>(1, 100, 0);
        quantity.setEditable(false);
        quantity.setDisable(true);

        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null){
                try{
                    int stock = DBConnect.getStock(newSelection.getName());
                    quantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stock, 1));
                    quantity.setDisable(false);}
                catch (SQLException ex){
                    throw new RuntimeException(ex);}}
            else{
                quantity.setDisable(true);}});

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            carts selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                try {
                    DBConnect.removeFromCart(email, selectedBook.getName());
                    booksTable.setItems(DBConnect.getCartItems(email));}
                catch (SQLException ex) {
                    throw new RuntimeException(ex);}}
            else {
                showWarning("You need to select one product");}});

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setOnAction(e -> {
            try {
                DBConnect.checkout(email);
                booksTable.setItems(DBConnect.getCartItems(email));}
            catch (SQLException ex) {
                throw new RuntimeException(ex);}});

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e ->{
            carts selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if(selectedBook != null){
                try{
                    DBConnect.updateCart(email, selectedBook.getName(), quantity.getValue());
                    booksTable.setItems(DBConnect.getCartItems(email));
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}
            }else{
                showWarning("You need to select one product");}});

        HBox buttons = new HBox(removeButton, checkoutButton, updateButton);
        buttons.setAlignment(Pos.BOTTOM_LEFT);

        VBox cart = new VBox(10, cartOwner, booksTable, quantity, buttons);
        cart.setAlignment(Pos.CENTER_LEFT);
        cart.setPadding(new Insets(10));
        VBox.setVgrow(booksTable, Priority.ALWAYS);
        VBox root = new VBox(mb, cart);
        Scene scene = new Scene(root, 1920, 1080);
        primaryStage.setTitle("Dollar Book Shop");
        primaryStage.setScene(scene);
        primaryStage.show();
        booksTable.setItems(DBConnect.getCartItems(email));
    }

    private TableView<carts> createTable(){
        TableView<carts> table = new TableView<>();
        TableColumn<carts, String> IdColumn = new TableColumn<>("Id");
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<carts, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<carts, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<carts, Integer> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<carts, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        TableColumn<carts, Integer> subtotalColumn = new TableColumn<>("Total");
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        table.getColumns().addAll(IdColumn, nameColumn, genreColumn, priceColumn, quantityColumn, subtotalColumn);

        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setPrefWidth(150);}
        return table;}

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();}

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();}
}
