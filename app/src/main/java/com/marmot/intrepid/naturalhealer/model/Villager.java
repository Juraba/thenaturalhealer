package com.marmot.intrepid.naturalhealer.model;

import java.util.ArrayList;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Villager {
    private String name;
    private ArrayList<Quest> quests;

    public Villager(String name){
        this.name = name;
    }

    public void addQuest(Quest quest){
        this.quests.add(quest);
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Quest> getQuests() {
        return this.quests;
    }
}
