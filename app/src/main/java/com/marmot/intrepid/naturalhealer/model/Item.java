package com.marmot.intrepid.naturalhealer.model;

public class Item {
    private String name, description, properties;
    private double price;
    private Rank rank;

    public Item(String name, String description, String properties, double price, Rank rank){
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.price = price;
        this.rank = rank;
    }

    public String getName() {return this.name;}

    public String getDescription() {return this.description;}

    public String getProperties() {return this.properties;}

    public double getPrice() {return this.price;}

    public Rank getRank() {return this.rank;}
}

