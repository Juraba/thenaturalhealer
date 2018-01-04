package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Item {
    @PrimaryKey
    @NonNull
    private String name;
    @ColumnInfo(name = "pic_name")
    private String picName;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "properties")
    private String properties;
    @ColumnInfo(name = "price")
    private double price;
    @ColumnInfo(name = "rank")
    private String rank;
    @ColumnInfo(name = "race")
    private String race;
    @ColumnInfo(name = "history")
    private String history;
    @ColumnInfo(name = "combination")
    private String combination;
    @ColumnInfo(name = "rarity")
    private String rarity;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "available")
    private int available;
    @ColumnInfo(name = "protocol")
    private String protocol;
    @ColumnInfo(name = "difficulty")
    private String difficulty;
    @ColumnInfo(name = "symptoms")
    private String symptoms;
    @ColumnInfo(name = "discovered")
    private int discovered;
    @ColumnInfo(name = "itemType")
    private String itemType;

    public Item(String name, String picName, String description, String properties, double price, String rank, String race, String history, String combination, String rarity, String type, int available, String protocol, String difficulty, String symptoms, int discovered, String itemType){
        this.name = name;
        this.picName = picName;
        this.description = description;
        this.properties = properties;
        this.price = price;
        this.rank = rank;
        this.race = race;
        this.rarity = rarity;
        this.history = history;
        this.combination = combination;
        this.type = type;
        this.available = available;
        this.difficulty = difficulty;
        this.protocol = protocol;
        this.symptoms = symptoms;
        this.discovered = discovered;
        this.itemType = itemType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getCombination() {
        return combination;
    }

    public void setCombination(String combination) {
        this.combination = combination;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public int getDiscovered() {
        return discovered;
    }

    public void setDiscovered(int discovered) {
        this.discovered = discovered;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
