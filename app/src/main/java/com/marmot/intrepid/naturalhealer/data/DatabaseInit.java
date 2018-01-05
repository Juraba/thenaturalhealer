package com.marmot.intrepid.naturalhealer.data;

import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;

import java.util.ArrayList;

/**
 * Created by Camille K on 05/01/2018.
 */

public class DatabaseInit implements Runnable{

    public void run(){

    }

    public void initQuestBook(DAOBase db, ArrayList<QuestBook> questBooks){
        for(QuestBook it : questBooks) {
            db.questBookDAO().insertQuestBook(it);
        }
    }

    public void initQuestList(DAOBase db, ArrayList<QuestList> questLists){
        for(QuestList it : questLists) {
            db.questListDAO().insertQuestList(it);
        }
    }

    public void initQuest(DAOBase db, ArrayList<Quest> quests){
        for(Quest it : quests) {
            db.questDAO().insertQuest(it);
        }
    }

    public void initVillager(DAOBase db, ArrayList<Villager> villagers){
        for(Villager it : villagers){
            db.villagerDAO().insertVillager(it);
        }
    }

    public void initItem(DAOBase db, ArrayList<Item> items){
        for(Item it : items){
            db.itemDAO().insertItem(it);
        }
    }

    public void initPlayer(DAOBase db, ArrayList<Player> players){
        for(Player it : players){
            db.playerDAO().insertOne(it);
        }
    }

    public void initInventory(DAOBase db, ArrayList<Inventory> inventories){
        for(Inventory it : inventories){
            db.inventoryDAO().insertInventory(it);
        }
    }
}
