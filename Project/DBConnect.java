package Project;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/dollarbookshop";
    private static final String USER = "root";
    private static final String PASSWORD ="Ayasjago1@";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);}

    public static boolean validateLogin(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;}}
        return false;}

    public static String getURole(String email, String password) throws SQLException{
        String query = "SELECT Role FROM users WHERE email = ? AND password = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,email);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);}}
        return null;}

    public static void addUser(String email, String password,String username, String dob) throws SQLException{
        String userID = generateID("users", "UserID", "US");
        String query = "INSERT INTO users(UserID, Email, Username, password, DOB, Role) VALUES(?,?,?,?,?,'user')";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,userID);
            ps.setString(2,email);
            ps.setString(3,username);
            ps.setString(4,password);
            ps.setString(5,dob);
            ps.executeUpdate();}
        catch (SQLException ex){
            throw new RuntimeException(ex);}}

    public static String generateID(String table, String column, String prefix) throws SQLException{
        String query = "SELECT MAX("+column+") FROM "+table;
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String lastID = rs.getString(1);
                if(lastID != null){
                    int idn = Integer.parseInt(lastID.replace(prefix, ""));
                    return prefix + String.format("%03d", idn+1);}}}
        return prefix + "001";}

    public static boolean checkEmail(String email) throws SQLException{
        String query = "SELECT * FROM users WHERE email = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            return rs.next();}}

    public static boolean checkUsername(String username) throws SQLException{
        String query = "SELECT * FROM users WHERE username = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            return rs.next();}}

    public static String getUsername(String email) throws SQLException{
        String query = "SELECT Username FROM users WHERE email = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString(1);}}
        return null;}

    public static ObservableList<books> getBooks() throws SQLException{
        ObservableList<books> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM products";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                books.add(new books(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));}
            return books;}}

    public static int getStock(String name) throws SQLException{
        String query = "SELECT Stock FROM products WHERE Name = ?";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);}}
        return 0;}

    public static int getCartQuantity(String email, String name) throws SQLException{
        String query = "SELECT Quantity FROM carts C JOIN users U ON C.UserID = U.UserID WHERE U.Email = ? AND C.ProductID = (SELECT ProductID FROM products WHERE Name = ?)";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,email);
            ps.setString(2,name);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);}}
        return 0;}

    public static void addtoCart(String email, String name, int quantity) throws SQLException {
        String query = "INSERT INTO carts (UserID, ProductID, Quantity) VALUES ((SELECT UserID FROM users WHERE Email = ?), (SELECT ProductID FROM products WHERE Name = ?), ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setInt(3, quantity);
            ps.executeUpdate();}}

    public static void updateCart(String email, String name, int quantity) throws SQLException {
        String query = "UPDATE carts SET Quantity = ? WHERE UserID = (SELECT UserID FROM users WHERE Email = ?) AND ProductID = (SELECT ProductID FROM products WHERE Name = ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setString(2, email);
            ps.setString(3, name);
            ps.executeUpdate();}}

    public static ObservableList<carts> getCartItems(String email) {
        ObservableList<carts> cartItems = FXCollections.observableArrayList();
        try{
            String query = "SELECT C.ProductID, P.Name, P.Genre, P.Price, C.Quantity, (P.Price * C.Quantity) AS Subtotal " +
                    "FROM carts C JOIN products P ON C.ProductID = P.ProductID " +
                    "JOIN users U ON C.UserID = U.UserID WHERE U.Email = ?";
            try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    cartItems.add(new carts(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));}}}
        catch (SQLException ex){
            ex.printStackTrace();}
        return cartItems;}

    public static void removeFromCart(String email, String name) throws SQLException {
        String query = "DELETE FROM carts WHERE UserID = (SELECT UserID FROM users WHERE Email = ?) AND ProductID = (SELECT ProductID FROM products WHERE Name = ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, name);
            ps.executeUpdate();}}

    public static String getUID(String email) throws SQLException {
        String query = "SELECT UserID FROM users WHERE Email = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString(1);}}
        return null;}

    public static void checkout(String email) throws SQLException {
        String TransID = generateID("transaction_header", "TransactionID", "TR");
        String UID = getUID(email);
        String query = "INSERT INTO transaction_header (TransactionID, UserID, TransactionDate) VALUES (?,?,CURDATE())";
        String query2 = "INSERT INTO transaction_details (TransactionID, ProductID, Quantity) SELECT ?, ProductID, Quantity FROM carts WHERE UserID = ?";
        String query3 = "DELETE FROM carts WHERE UserID = ?";
        String query4 = "UPDATE products P JOIN carts C ON P.ProductID = C.ProductID SET P.Stock = P.Stock - C.Quantity WHERE C.UserID = ?";
        try (Connection conn = connect();PreparedStatement ps = conn.prepareStatement(query);PreparedStatement ps2 = conn.prepareStatement(query2);PreparedStatement ps3 = conn.prepareStatement(query3);PreparedStatement ps4 = conn.prepareStatement(query4)){
            ps.setString(1, TransID);
            ps.setString(2, UID);
            ps.executeUpdate();
            ps2.setString(1, TransID);
            ps2.setString(2, UID);
            ps2.executeUpdate();
            ps4.setString(1, UID);
            ps4.executeUpdate();
            ps3.setString(1, UID);
            ps3.executeUpdate();}}

    public static void deleteBook(String name) throws SQLException {
        String query = "DELETE FROM products WHERE Name = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();}}

    public static void addBook(String name, String genre, int stock, int price) throws SQLException{
        String id = generateID("products", "ProductID", "PD");
        String query = "INSERT INTO products (ProductID, Name, Genre, Stock, Price) VALUES (?,?,?,?,?)";
        try(Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1,id);
            ps.setString(2,name);
            ps.setString(3,genre);
            ps.setInt(4,stock);
            ps.setInt(5,price);
            ps.executeUpdate();}
        catch (SQLException ex){
            throw new RuntimeException(ex);}
    }

    public static void updateBook(String id, String name, String genre, int stock, int price) throws SQLException {
        String query = "UPDATE products SET Name = ?, Genre = ?, Stock = ?, Price = ? WHERE ProductID = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, genre);
            ps.setInt(3, stock);
            ps.setInt(4, price);
            ps.setString(5, id);
            ps.executeUpdate();}}

}
