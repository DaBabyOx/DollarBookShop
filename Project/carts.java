package Project;

public class carts {
    private String id, name, genre;
    private int price, quantity, subtotal;

    public carts(String id, String name, String genre, int price, int quantity, int subtotal){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;}

    public String getId() {
        return id;}

    public String getName() {
        return name;}

    public String getGenre() {
        return genre;}

    public int getPrice() {
        return price;}

    public int getQuantity() {
        return quantity;}

    public int getSubtotal() {
        return subtotal;}}
