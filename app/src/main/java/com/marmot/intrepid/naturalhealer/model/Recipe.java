package com.marmot.intrepid.naturalhealer.model;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Recipe extends Item {
    private String difficulty;
    private String protocol;
    private String[] symptoms;
    private boolean discovered, available;

    public Recipe(String name, String desc, String properties, double price, Rank rank, String difficulty, String[] symptoms, String protocol){
        super(name, desc, properties, price, rank);
        this.difficulty = difficulty;
        this.protocol = protocol;
        this.symptoms = symptoms;
        this.discovered = false;
        this.available = false;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public  String getProtocol() {
        return protocol;
    }

    public String[] getSymptoms() {
        return symptoms;
    }

    public void setDiscover(){
        if(!this.discovered){
            this.discovered = true;
        }
    }

    public boolean isDiscovered(){
        return discovered;
    }

    public void setAvailable(){
        if(!this.available){
            this.available = true;
        }
    }

    public boolean isAvailable() {
        return available;
    }
}
