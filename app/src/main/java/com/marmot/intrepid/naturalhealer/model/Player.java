package com.marmot.intrepid.naturalhealer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String nickname;
    private int xp;
    private double purse;
    private Rank rank;
    private HashMap<Item, Integer> inventory;
    private HashMap<String, Quest> quests;

    public Player(Rank rank, String nickname, int exp, double purse){
        this.nickname = nickname;
        this.xp = exp;
        this.purse = purse;
        this.rank = rank;
        this.inventory = new HashMap<Item, Integer>();
        this.quests = new HashMap<String, Quest>();
    }

    public void acceptQuest(Quest quest, String villagerName){
        this.quests.put(villagerName, quest);
    }

    public void cancelQuest(Quest quest){
        this.quests.remove(quest);
    }

    public void brew(ArrayList<Item> ingredients){

    }

    public void addItems(Item item, int number){
        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            int value = i.getValue();

            if (key.getName().equals(item.getName())) {
                value += number;
            } else {
                this.inventory.put(item, number);
            }
        }
    }

    public void sellItems(Item item, int number){
        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            int value = i.getValue();

            if (key.getName().equals(item.getName())) {
                if ((value-number) < 0) {
                    //toast "wat r u doin bro ?!!!!" et ne pas lancer l'action
                } else if ((value-number) > 0) {
                    value -= number;
                } else {
                    removeItem(item);
                }
            } else {
                this.inventory.put(item, number);
            }
        }
    }

    public void removeItem(Item item){
        inventory.remove(item);
    }

    public void explore(){

    }
}
