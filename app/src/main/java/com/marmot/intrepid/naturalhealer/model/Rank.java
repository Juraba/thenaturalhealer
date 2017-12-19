package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;

/**
 * Created by Camille K on 19/12/2017.
 */

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
}