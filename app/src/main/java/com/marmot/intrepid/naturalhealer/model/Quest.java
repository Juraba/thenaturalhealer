package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;

import java.util.HashMap;

@Entity
public class Quest {
    @PrimaryKey
    @NonNull
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @Ignore
    private QuestType type;
    @ColumnInfo(name = "quest_type")
    private String questType;
    @Ignore
    private HashMap<Item, Integer> requirements;
    @ColumnInfo(name = "reward_money")
    private int rewardMoney;
    @ColumnInfo(name = "reward_xp")
    private int rewardXp;
    @Ignore
    private boolean cancelable, done;
    @ColumnInfo(name = "cancelable")
    private String cancelableString;
    @ColumnInfo(name = "done")
    private String doneString;

    public Quest(String name, String description, String questType, int rewardMoney, int rewardXp, String cancelableString, String doneString){
        this.name = name;
        this.description = description;
        this.requirements = null;
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelableString = cancelableString;
        if(this.cancelableString.equals("true")){
            this.cancelable = true;
        }
        else {
            this.cancelable = false;
        }
        this.doneString = doneString;
        if(this.doneString.equals("true")){
            this.done = true;
        }
        else {
            this.done = false;
        }
        this.questType = questType;
        this.type = QuestType.findEn(questType);
    }

    @Ignore
    public Quest(String name, String description, HashMap<Item, Integer> requirements, int rewardMoney, int rewardXp, boolean cancelable, QuestType type){
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelable = cancelable;
        if(cancelable){
            this.cancelableString = "true";
        }
        else {
            this.cancelableString = "false";
        }
        this.done = false;
        if(done){
            this.doneString = "true";
        }
        else {
            this.doneString = "false";
        }
        this.type = type;
        this.questType = type.getEn();
    }

    public String getName() {return this.name;}

    public String getDescription() {return this.description;}

    public QuestType getType() {return this.type;}

    public int getRewardMoney() {return this.rewardMoney;}

    public int getRewardXp() {return this.rewardXp;}

    public boolean isDone(){return this.done;}

    public boolean isCancelable(){return this.cancelable;}

    public HashMap<Item, Integer> getRequirements() {
        return requirements;
    }

    public void setRequirements(HashMap<Item, Integer> requirements){this.requirements = requirements;}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestType() {
        return questType;
    }

    public void setQuestType(String questType) {
        this.questType = questType;
    }

    public void setRewardMoney(int rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public void setRewardXp(int rewardXp) {
        this.rewardXp = rewardXp;
    }

    public String getCancelableString() {
        return cancelableString;
    }

    public void setCancelableString(String cancelableString) {
        this.cancelableString = cancelableString;
    }

    public String getDoneString() {
        return doneString;
    }

    public void setDoneString(String doneString) {
        this.doneString = doneString;
    }
}
