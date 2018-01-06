package com.marmot.intrepid.naturalhealer.data;

import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;

import java.util.ArrayList;

/**
 * Created by Camille K on 05/01/2018.
 */

public class DatabaseUpdate implements Runnable{

    public void run(){

    }

    public void updateQuestBook(DAOBase db, ArrayList<QuestBook> questBooks){
        for(QuestBook it : questBooks) {
            db.questBookDAO().updateQuestBook(it);
        }
    }

    public void updateQuestList(DAOBase db, ArrayList<QuestList> questLists){
        for(QuestList it : questLists) {
            db.questListDAO().updateQuestList(it);
        }
    }

    public void updateQuest(DAOBase db, ArrayList<Quest> quests){
        for(Quest it : quests) {
            db.questDAO().updateQuest(it);
        }
    }

    public void updateVillager(DAOBase db, ArrayList<Villager> villagers){
        for(Villager it : villagers){
            db.villagerDAO().updateVillager(it);
        }
    }

    public void updateItem(DAOBase db, ArrayList<Item> items){
        for(Item it : items){
            db.itemDAO().updateItem(it);
        }
    }

    public void updatePlayer(DAOBase db, ArrayList<Player> players){
        for(Player it : players){
            db.playerDAO().updatePlayer(it);
        }
    }

    public void updateInventory(DAOBase db, ArrayList<Inventory> inventories){
        for(Inventory it : inventories){
            db.inventoryDAO().updateInventory(it);
        }
    }
}
