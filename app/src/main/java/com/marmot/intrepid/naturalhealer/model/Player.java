package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.marmot.intrepid.naturalhealer.service.GameService;

@Entity
public class Player {
    @PrimaryKey
    @NonNull
    private String nickname;
    @ColumnInfo(name = "pic_name")
    private String picName;
    @ColumnInfo(name = "xp")
    private int xp;
    @ColumnInfo(name = "purse")
    private double purse;
    @ColumnInfo(name = "rank")
    private String rankName;
    @Ignore
    private Rank rank;
    @Ignore
    private HashMap<Item, Integer> inventory;
    @Ignore
    private HashMap<String, Quest> quests;

    public Player(String nickname, String picName, int xp, double purse, String rankName){
        this.nickname = nickname;
        this.picName = picName;
        this.xp = xp;
        this.purse = purse;
        this.rank = new Rank(RankEnum.findEn(rankName));
        this.rankName = rankName;
        this.inventory = new HashMap<Item, Integer>();
        this.quests = new HashMap<String, Quest>();
    }

    @Ignore
    public Player(String nickname, String picName, Rank rank, int exp, double purse){
        this.nickname = nickname;
        this.picName = picName;
        this.xp = exp;
        this.purse = purse;
        this.rank = rank;
        this.rankName = rank.getName().getEn();
        this.inventory = new HashMap<Item, Integer>();
        this.quests = new HashMap<String, Quest>();
    }

    public String getNickname() {return this.nickname;}

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPicName() {return this.picName;}

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setPurse(double purse) {
        this.purse = purse;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

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
        boolean check = false;
        int value = number;
        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            value = i.getValue();

            if (key.getName().equals(item.getName())) {
                value += number;
                check = true;
            } else {
                value = number;
            }
        }
        if (!check) {
            this.inventory.put(item, value);
        }
    }

    public String buyItems(Item item, int number) {
        boolean check = false;
        int value = number;
        String render = "";

        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            value = i.getValue();

            if (key.getName().equals(item.getName())) {
                check = true;
                if ((this.getPurse()-(number*item.getPrice())) >= 0) {
                    value += number;
                    this.setPurse(this.getPurse()-(number*item.getPrice()));
                } else {
                    render = "You don't have enough money ! ";
                }
            }
        }
        if (!check) {
            if ((this.getPurse()-(number*item.getPrice())) >= 0) {
                this.inventory.put(item, value);
                this.setPurse(this.getPurse()-(number*item.getPrice()));
            } else {
                render = "You don't have enough money ! ";
            }
        }

        return render;
    }

    // A modifier !
    public String sellItems(Item item, int number){
        boolean check = false;
        int value = number;
        String render = "";

        for (Map.Entry<Item, Integer> i : inventory.entrySet()) {
            Item key = i.getKey();
            value = i.getValue();

            if (key.getName().equals(item.getName())) {
                if ((this.getPurse()-(number*item.getPrice())) >= 0) {
                    if ((value-number) < 0) {
                        render = "You don't have enough of this item to sell it ! ";
                    } else if ((value-number) > 0) {
                        value -= number;
                        this.setPurse(this.getPurse()-(number*item.getPrice()));
                    } else if ((value-number) == 0){
                        check = true;
                        this.setPurse(this.getPurse()-(number*item.getPrice()));
                    }
                } else {
                    render = "You don't have enough money ! ";
                }
            }
        }
        if (check) {
            this.inventory.remove(item);
        }

        return render;
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

    public void setInventory(HashMap<Item, Integer> inventory) {
        this.inventory = inventory;
    }

    public void setQuests(HashMap<String, Quest> quests) {
        this.quests = quests;
    }
}
