package Project;

import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class AdminHome {
    private TableView<books> booksTable;

    public AdminHome(Stage primaryStage) {
        MenuBar mb = MenuBar.createMenuBar(primaryStage, "AdminHome");
        Label welcomeLabel = new Label("Welcome Back, Admin");
        welcomeLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");

        booksTable = createTable();
        booksTable.setPrefHeight(500);
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(booksTable, Priority.ALWAYS);

        VBox productLayout = new VBox(5, welcomeLabel, booksTable);
        productLayout.setAlignment(Pos.CENTER_LEFT);
        productLayout.setPadding(new Insets(10));

        Label name = new Label("Name");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setMaxWidth(200);

        Label genre = new Label("Genre");
        TextField genreField = new TextField();
        genreField.setPromptText("Genre");
        genreField.setMaxWidth(200);

        Label stock = new Label("Stock");
        Spinner<Integer> stockSpinner = new Spinner<>(0, 100, 0);
        stockSpinner.setEditable(true);

        Label price = new Label("Price");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setMaxWidth(200);

        Button deleteButton = new Button("Delete");
        deleteButton.setMinWidth(20);
        deleteButton.setOnAction(e ->{
            books selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if(selectedBook != null){
                try{
                    DBConnect.deleteBook(selectedBook.getId());
                    booksTable.setItems(DBConnect.getBooks());
                    booksTable.refresh();
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}
            }else{
                showWarning("You need to select one product");}});


        Button addButton = new Button("Add");
        addButton.setMinWidth(20);
        addButton.setOnAction(e -> {
            if (nameField.getText().isEmpty() || genreField.getText().isEmpty() ||(stockSpinner.getValue().equals(0)) || priceField.getText().isEmpty()) {
                showWarning("Please fill in all fields!");
            } else {
                try {
                    int pricing = Integer.parseInt(priceField.getText());
                    if (pricing < 0) {
                        showWarning("Price must be positive!");
                        return;
                    }
                    DBConnect.addBook(nameField.getText(), genreField.getText(), stockSpinner.getValue(), pricing);
                    booksTable.setItems(DBConnect.getBooks());
                    booksTable.refresh();
                } catch (NumberFormatException ex) {
                    showWarning("Price must be a valid number!");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        Button updateButton = new Button("Update");
        updateButton.setMinWidth(20);
        updateButton.setOnAction(e ->{
            books selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if(nameField.getText().isEmpty() || genreField.getText().isEmpty() || priceField.getText().isEmpty()){
                showWarning("Please fill in all fields!");return;}

            if(selectedBook != null){
                try {
                    DBConnect.updateBook(selectedBook.getId(), nameField.getText(), genreField.getText(), stockSpinner.getValue(), Integer.parseInt(priceField.getText()));
                    booksTable.setItems(DBConnect.getBooks());
                    booksTable.refresh();
                }catch (NumberFormatException ex){
                    showWarning("Price must be numbers!");
                }catch(SQLException ex){
                    throw new RuntimeException(ex);}
            }else{
                showWarning("You need to select one product");}});

        HBox buttons = new HBox(10, deleteButton, addButton, updateButton);
        buttons.setAlignment(Pos.CENTER);

        VBox forms = new VBox(5, name, nameField, genre, genreField, stock, stockSpinner, price, priceField, buttons);
        forms.setAlignment(Pos.CENTER_LEFT);

        HBox centredForms = new HBox(forms);
        centredForms.setAlignment(Pos.CENTER);

        VBox layout = new VBox(5, productLayout, centredForms);
        layout.setAlignment(Pos.CENTER);

        VBox finalLayout = new VBox(10, mb, welcomeLabel, layout);

        try{
            ObservableList<books> books = DBConnect.getBooks();
            booksTable.setItems(books);
        }catch(SQLException e){
            e.printStackTrace();}

        booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getName());
                genreField.setText(newSelection.getGenre());
                stockSpinner.getValueFactory().setValue(newSelection.getStock());
                priceField.setText(String.valueOf(newSelection.getPrice()));
            }});

        Scene scene = new Scene(finalLayout, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dollar Book Shop");
        primaryStage.show();}

    private TableView<books> createTable(){
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
            column.setPrefWidth(150);}
        return table;}

    private void showWarning(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Warning");
        alert.setContentText(message);
        alert.showAndWait();}
}
