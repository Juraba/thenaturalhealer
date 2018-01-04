package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.Entity;

import com.marmot.intrepid.naturalhealer.model.enumerations.RecipeDifficulty;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;

@Entity
public class Recipe extends Item {
    private String protocol;
    private RecipeDifficulty difficulty;
    private Symptoms[] symptoms;
    private boolean discovered, available;

    public Recipe(String name, String picName, String desc, String properties, double price, Rank rank, RecipeDifficulty difficulty, Symptoms[] symptoms, String protocol){
        super(name, picName, desc, properties, price, rank);
        this.difficulty = difficulty;
        this.protocol = protocol;
        this.symptoms = symptoms;
        this.discovered = false;
        this.available = false;
    }

    public RecipeDifficulty getDifficulty() {return this.difficulty;}

    public  String getProtocol() {return this.protocol;}

    public Symptoms[] getSymptoms() {return this.symptoms;}

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
