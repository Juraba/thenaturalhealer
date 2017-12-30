package com.marmot.intrepid.naturalhealer.model;

import android.graphics.Bitmap;

import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.marmot.intrepid.naturalhealer.service.GameService;

public class Player {
    private String nickname;
    private Bitmap pic;
    private int xp;
    private double purse;
    private Rank rank;
    private HashMap<Item, Integer> inventory;
    private HashMap<String, Quest> quests;

    public Player(String nickname, Bitmap pic, Rank rank, int exp, double purse){
        this.nickname = nickname;
        this.pic = pic;
        this.xp = exp;
        this.purse = purse;
        this.rank = rank;
        this.inventory = new HashMap<Item, Integer>();
        this.quests = new HashMap<String, Quest>();
    }

    public String getNickname() {return this.nickname;}

    public Bitmap getPic() {return this.pic;}

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

    public void explore(){

    }

    public void brew(ArrayList<Item> ingredients){

    }

    /*
    public static Player loadPlayer() {
        Bitmap pic = GameService.getInstance().getPic();
        return new Player("UnPseudoLambda", pic, new Rank(RankEnum.RECRUIT), 0, 500.00);
    }
    */
}
