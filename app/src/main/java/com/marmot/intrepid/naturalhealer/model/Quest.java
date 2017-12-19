
package com.marmot.intrepid.naturalhealer.model;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Quest {
    private String description;
    private int[] requirements;
    private float[] goals;
    private int rewardMoney;
    private int rewardXp;
    private boolean cancelable;
    private boolean done;
    private String type;

    public Quest(String description, int[] requirements, float[] goals, int rewardMoney, int rewardXp, boolean cancelable, boolean done, String type){
        this.description = description;
        this.requirements = requirements;
        this.goals = goals;
        this.rewardMoney = rewardMoney;
        this.rewardXp = rewardXp;
        this.cancelable = cancelable;
        this.done = done;
        this.type = type;
    }

    //TODO : écrire methodes
    public boolean isDone(){return this.done;}
    public void fill(int[] requirements){}
    public void cancel(){}
}
