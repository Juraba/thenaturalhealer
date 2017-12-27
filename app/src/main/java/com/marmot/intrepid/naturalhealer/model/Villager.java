package com.marmot.intrepid.naturalhealer.model;

import java.util.ArrayList;

public class Villager {
    private String name;
    private ArrayList<Quest> quests;

    public Villager(String name){
        this.name = name;
    }

    public String getName() {return this.name;}

    public ArrayList<Quest> getQuests() {return this.quests;}

    public void addQuest(Quest quest){this.quests.add(quest);}
}
