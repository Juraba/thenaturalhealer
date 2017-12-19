package com.marmot.intrepid.naturalhealer.model;

import java.util.ArrayList;

public class Player {
    private String nickname;
    private int experience;
    private double purse;
    private ArrayList<Item> inventory;
    private ArrayList<Quest> quests;

    public Player(ArrayList<Item> inventory, ArrayList<Quest> quests, Rank rank, String nickname, int experience, double purse){
        this.inventory = inventory;
        this.quests = quests;
        this.nickname = nickname;
        this.experience = experience;
        this.purse = purse;
    }

    public void acceptQuest(Quest quest, String villagerName){

    }

    //TODO : écrire methodes
    public void cancelQuest(Quest quest){}
    public void brew(ArrayList<Item> ingredients){}
    public void addItem(Item item){}
    public void addItems(Item item, int number){}
    public void sellItem(Item item){}
    public void sellItems(Item item, int number){}
    public void removeItem(Item item){}
    public void explore(){}
}
