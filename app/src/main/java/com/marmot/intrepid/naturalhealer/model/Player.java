package com.marmot.intrepid.naturalhealer.model;

import android.graphics.Bitmap;

import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.marmot.intrepid.naturalhealer.service.GameService;

public class Player {
    private String nickname;
    private String picName;
    private int xp;
    private double purse;
    private Rank rank;
    private HashMap<Item, Integer> inventory;
    private HashMap<String, Quest> quests;

    public Player(String nickname, String picName, Rank rank, int exp, double purse){
        this.nickname = nickname;
        this.picName = picName;
        this.xp = exp;
        this.purse = purse;
        this.rank = rank;
        this.inventory = new HashMap<Item, Integer>();
        this.quests = new HashMap<String, Quest>();
    }

    public String getNickname() {return this.nickname;}

    public String getPicName() {return this.picName;}

    public int getXp() {return this.xp;}

    public double getPurse() {return this.purse;}

    public Rank getRank() {return this.rank;}

    public HashMap<Item, Integer> getInventory() {return this.inventory;}

    public HashMap<String, Quest> getQuests() {return this.quests;}

    public void acceptQuest(Quest quest, String villagerName){
        this.quests.put(villagerName, quest);
    }

    public void cancelQuest(Quest quest){
        if (quest.isCancelable()) {
            this.quests.remove(quest);
        } else {
            //Mettre un mécanisme qui ne retire pas la quête mais sort un toast qui dit qu'on peut pas supprimer la quête
        }
    }

    public void removeQuest(Quest quest) {
        if (quest.isDone()) {
            this.quests.remove(quest);
        } else {
            //Ne remove pas la quête, pas de toast
        }
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

    public static HashMap<Item, Integer> explore(){
        ArrayList<Herb> herbs = new ArrayList<Herb>();
        HashMap<Item, Integer> rewards = new HashMap<>();
        int rewardNumber = (int) ((Math.random()*5) +1);
        for(int i=0; i < rewardNumber; i++){
            int herb = (int) (Math.random()*(herbs.size())); //int herb = (int) (Math.random()(herbs.size()));
            Integer nbHerb = new Integer((int) (Math.random()*5)+1);
            rewards.put(herbs.get(herb), nbHerb); //rewards.push(herbs.get(herb), nbHerb);
        }
        return rewards;
    }

    public void brew(ArrayList<Item> ingredients){

    }

    //Méthode temporaire
    public static Player loadPlayer() {
        return new Player("Jean-Michel Druide", "ic_player", new Rank(RankEnum.APPRENTICE), 930, 500.00);
    }
}
