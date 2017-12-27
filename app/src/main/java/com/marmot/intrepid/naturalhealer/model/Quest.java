package com.marmot.intrepid.naturalhealer.model;

public class Quest {
    private String description, type;
    private int[] requirements;
    private float[] goals;
    private int rewardMoney, rewardXp;
    private boolean cancelable, done;

    public Quest(String description, int[] requirements, float[] goals, int rewardMoney, int rewardXp, boolean cancelable, String type){
        this.description = description;
        this.requirements = requirements;
        this.goals = goals;
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelable = cancelable;
        this.done = false;
        this.type = type;
    }

    public String getDescription() {return this.description;}

    public String getType() {return this.type;}

    public int getRewardMoney() {return this.rewardMoney;}

    public int getRewardXp() {return this.rewardXp;}

    public boolean isDone(){return this.done;}

    public boolean isCancelable(){return this.cancelable;}

    public void fill(int[] requirements){}

}
