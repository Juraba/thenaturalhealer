package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

public class Rank {
    private RankEnum name;
    private int goal;

    public Rank(RankEnum name){
        this.name = name;

        switch (this.name) {
            case RECRUIT:
                this.goal = 500;
                break;
            case JUNIOR:
                this.goal = 1000;
                break;
            case APPRENTICE:
                this.goal = 2000;
                break;
            case VETERAN:
                this.goal = 5000;
                break;
        }
    }

    public int getGoal() {return this.goal;}

    public RankEnum getName() {return this.name;}
}