package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.*;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.HashMap;

public class DatabaseSavePlayer implements Runnable{

    @Override
    public void run() {
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        //Getting game instance
        GameService game = GameService.getInstance();
        savePlayer(db, game.getPlayer());
        db.close();
    }

    public void savePlayer(DAOBase db, Player player){
        //Saving player
        Player p = new Player(player.getNickname(), player.getPicName(),player.getXp(), player.getPurseString(), player.getRankName());
        db.playerDAO().insertOrUpdate(p);

        //Saving player's inventory
        HashMap<com.marmot.intrepid.naturalhealer.model.Item, Integer> inventory = player.getInventory();
        for(HashMap.Entry<com.marmot.intrepid.naturalhealer.model.Item, Integer> entry : inventory.entrySet()){
            if(entry.getKey().getClass() == Herb.class){
                Inventory i = new Inventory(0, player.getNickname(), entry.getKey().getName(), "herb", entry.getValue());
                db.inventoryDAO().insertOrUpdate(i);
            }
            else if(entry.getKey().getClass() == Recipe.class){
                Inventory i = new Inventory(0, player.getNickname(), entry.getKey().getName(), "recipe", entry.getValue());
                db.inventoryDAO().insertOrUpdate(i);
            }
            else if(entry.getKey().getClass() == OtherIngredients.class){
                Inventory i = new Inventory(0,player.getNickname(), entry.getKey().getName(), "other", entry.getValue());
                db.inventoryDAO().insertOrUpdate(i);
            }
        }

        //Saving player's questbook
        HashMap<String, Quest> questbook = player.getQuests();
        for(HashMap.Entry<String, Quest> entry : questbook.entrySet()){
            QuestBook q = new QuestBook(0, player.getNickname(), entry.getKey(), entry.getValue().getName());
            System.out.println(player.getNickname());
            System.out.println(entry.getKey());
            System.out.println(entry.getValue().getName());
            db.questBookDAO().insertOrUpdate(q);
        }
    }
}
