package com.marmot.intrepid.naturalhealer.model;

public class Item {
    private String name;
    private String description;
    private String properties;
    private double price;
    private Rank rank;

    public Item(String name, String description, String properties, double price, Rank rank){
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.price = price;
        this.rank = rank;
    }

    public String getName() {
        return this.name;
    }
}

