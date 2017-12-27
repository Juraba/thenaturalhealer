package com.marmot.intrepid.naturalhealer.model;

public class Herb extends Item {
    private String race, rarity, history, combination, type;
    private boolean available;

    public Herb(String name, String desc, String properties, double price, Rank rank, String race, String rarity, String history, String combination, String type){
        super(name, desc, properties, price, rank);
        this.race = race;
        this.rarity = rarity;
        this.history = history;
        this.combination = combination;
        this.type = type;
        this.available = false;
    }

    public String getRace() {return this.race;}

    public String getRarity() {return this.rarity;}

    public String getHistory() {return this.history;}

    public String getCombination() {return this.combination;}

    public String getType() {return type;}

    public boolean isAvailable(){return available;}

    public void setAvailable(){
        if(!this.available){
            this.available = true;
        }
    }

}
