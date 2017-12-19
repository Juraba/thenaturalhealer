package com.marmot.intrepid.naturalhealer.model;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Herb extends Item {
    private String race;
    private String rarity;
    private String history;
    private String combination;
    private String type;
    private boolean available;

    public Herb(String name, String desc, String properties, double price, Rank rank, String race, String rarity, String history, String combination, String type){
        super(name, desc, properties, price, rank);
        this.race = race;
        this.rarity = rarity;
        this.history = history;
        this.combination = combination;
        this.type = type;
    }

    public String getRace() {
        return race;
    }

    public String getRarity() {
        return rarity;
    }

    public String getHistory() {
        return history;
    }

    public String getCombination() {
        return combination;
    }

    public String getType() {
        return type;
    }

    public void setAvailable(){
        if(!this.available){
            this.available = true;
        }
    }

    public boolean isAvailable(){
        return available;
    }

}
