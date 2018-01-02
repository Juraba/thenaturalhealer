package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.HerbRarity;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbType;

public class  Herb extends Item {
    private String race, history, combination;
    private HerbRarity rarity;
    private HerbType type;
    private boolean available;

    public Herb(String name, String picName, String desc, String properties, double price, Rank rank, String race, HerbRarity rarity, String history, String combination, HerbType type){
        super(name, picName, desc, properties, price, rank);
        this.race = race;
        this.rarity = rarity;
        this.history = history;
        this.combination = combination;
        this.type = type;
        this.available = false;
    }

    public String getRace() {return this.race;}

    public HerbRarity getRarity() {return this.rarity;}

    public String getHistory() {return this.history;}

    public String getCombination() {return this.combination;}

    public HerbType getType() {return type;}

    public boolean isAvailable(){return available;}

    public void setAvailable(){
        if(!this.available){
            this.available = true;
        }
    }

}
