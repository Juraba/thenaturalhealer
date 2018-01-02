package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;

public class Quest {
    private String name, description;
    private QuestType type;
    private int[] requirements;
    private float[] goals;
    private int rewardMoney, rewardXp;
    private boolean cancelable, done;

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
