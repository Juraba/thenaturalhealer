package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;

@Entity
public class Quest {
    @PrimaryKey
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @Ignore
    private QuestType type;
    @ColumnInfo(name = "quest_type")
    private String questType;
    @Ignore
    private int[] requirements;
    @ColumnInfo(name = "requirements")
    private String requirementsString;
    @Ignore
    private float[] goals;
    @ColumnInfo(name = "goals")
    private String goalsString;
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

    public Quest(String name, String description, int[] requirements, float[] goals, int rewardMoney, int rewardXp, boolean cancelable, QuestType type){
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.goals = goals;
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelable = cancelable;
        this.done = false;
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

    public int[] getRequirements() {
        return requirements;
    }

    public float[] getGoals() {
        return goals;
    }

    public void fill(int[] requirements){}

}
