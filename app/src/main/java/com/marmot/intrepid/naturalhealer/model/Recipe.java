package com.marmot.intrepid.naturalhealer.model;

public class Recipe extends Item {
    private String difficulty, protocol;
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

    public String getDifficulty() {return this.difficulty;}

    public  String getProtocol() {return this.protocol;}

    public String[] getSymptoms() {return this.symptoms;}

    public boolean isDiscovered(){return this.discovered;}

    public boolean isAvailable() {return this.available;}

    public void setDiscover(){
        if(!this.discovered){
            this.discovered = true;
        }
    }

    public void setAvailable(){
        if(!this.available){
            this.available = true;
        }
    }

}
