package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;

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

    public Quest(String name, String description, String questType, String requirementsString, String goalsString, int rewardMoney, int rewardXp, String cancelableString, String doneString){
        this.name = name;
        this.description = description;
        this.requirementsString = requirementsString;
        String[] tmp = requirementsString.split("\\§");
        this.requirements = new int[tmp.length];
        for(int i=0; i < tmp.length; i++){
            this.requirements[i] = Integer.parseInt(tmp[i]);
        }
        this.goalsString = goalsString;
        tmp = goalsString.split("\\§");
        this.goals = new float[tmp.length];
        for(int i=0; i < tmp.length; i++){
            this.goals[i] = Float.parseFloat(tmp[i]);
        }
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelableString = cancelableString;
        this.doneString = doneString;

        this.questType = questType;
        this.type = QuestType.findEn(questType);
    }

    @Ignore
    public Quest(String name, String description, int[] requirements, float[] goals, int rewardMoney, int rewardXp, boolean cancelable, QuestType type){
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.requirementsString = requirements.toString();
        this.goals = goals;
        this.goalsString = goals.toString();
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

    public String getRequirementsString() {
        return requirementsString;
    }

    public void setRequirementsString(String requirementsString) {
        this.requirementsString = requirementsString;
    }

    public String getGoalsString() {
        return goalsString;
    }

    public void setGoalsString(String goalsString) {
        this.goalsString = goalsString;
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
