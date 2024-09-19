package Project;

public class books {
    private String id, name, genre;
    private int price, stock;

    public books(String id, String name, String genre, int stock, int price){
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.stock = stock;
        this.price = price;
    }

    public String getId(){
        return id;}

    public String getName(){
        return name;}

    public String getGenre(){
        return genre;}

    public int getPrice(){
        return price;}

    public int getStock(){
        return stock;}}
