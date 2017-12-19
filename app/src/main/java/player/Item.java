package player;


public class Item {
    private String name;
    private String properties;
    private double price;
    private Rank rank;

    public Item(String name, String properties, double price, Rank rank){
        this.name = name;
        this.properties = properties;
        this.price = price;
        this.rank = rank;
    }
}

