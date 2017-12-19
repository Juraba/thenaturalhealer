package com.marmot.intrepid.naturalhealer.model;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Herb {
    private String race;
    private String rarity;
    private String history;
    private String combination;
    private String type;
    //Ajouter discover
    private boolean discover;

    public Herb(String race, String rarity, String history, String combination, String type){
        this.race = race;
        this.rarity = rarity;
        this.history = history;
        this.combination = combination;
        this.type = type;
        this.discover = false;
    }

    public void setDiscover(){
        if(!this.discover){
            this.discover = true;
        }
    }

    public boolean isDiscovered(){
        return discover;
    }


}
