package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.*;
import com.marmot.intrepid.naturalhealer.model.Item;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseSave implements Runnable{

    public void run(){
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        //Getting game instance
        GameService game = GameService.getInstance();
        saveVillager(db, game.getVillagers());
        saveItem(db, game.getItems());
        savePlayer(db, game.getPlayer());
        db.close();
    }

    public void savePlayer(DAOBase db, Player player){
        //Saving player
        Player p = new Player(player.getNickname(), player.getPicName(),player.getXp(), player.getPurseString(), player.getRankName());
        db.playerDAO().insertOrUpdate(p);

        //Saving player's inventory
        HashMap<Item, Integer> inventory = player.getInventory();
        for(HashMap.Entry<Item, Integer> entry : inventory.entrySet()){
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

    public void saveVillager(DAOBase db, ArrayList<Villager> villagers){
        for(Villager v : villagers){
            db.villagerDAO().insertOrUpdate(v);
            ArrayList<Quest> vQuests = v.getQuests();
            for(Quest q : vQuests){
                QuestList ql = new QuestList(0,v.getName(), q.getName());
                db.questListDAO().insertOrUpdate(ql);
                db.questDAO().insertOrUpdate(q);

                HashMap<Item, Integer> requirements = q.getRequirements();
                for(HashMap.Entry<Item, Integer> r : requirements.entrySet()){
                    Requirements requirement = new Requirements(0, q.getName(), r.getKey().getName(), r.getValue());
                    db.requirementsDAO().insertOrUpdate(requirement);
                }
            }
        }
    }

    public void saveItem(DAOBase db, ArrayList<Item> items){
        for(Item i : items){
            com.marmot.intrepid.naturalhealer.data.Item item = null;
            if(i.getClass() == Herb.class) {
                Herb h = (Herb) i;
                if (h.isAvailable()) {
                    item = new com.marmot.intrepid.naturalhealer.data.Item(i.getName(), i.getPicName(), i.getDescription(), i.getProperties(), Double.toString(i.getPrice()), i.getRank().getName().getEn(), ((Herb) i).getRace(), ((Herb) i).getHistory(), ((Herb) i).getCombination(), ((Herb) i).getRarity().getEn(), ((Herb) i).getType().getEn(), 1, "", "", "", 0, "herb");
                } else {
                    item = new com.marmot.intrepid.naturalhealer.data.Item(i.getName(), i.getPicName(), i.getDescription(), i.getProperties(), Double.toString(i.getPrice()), i.getRank().getName().getEn(), ((Herb) i).getRace(), ((Herb) i).getHistory(), ((Herb) i).getCombination(), ((Herb) i).getRarity().getEn(), ((Herb) i).getType().getEn(), 0, "", "", "", 0, "herb");
                }
            }
            else if(i.getClass() == Recipe.class){
                Recipe r = (Recipe) i;
                Symptoms[] symptoms = r.getSymptoms();
                String symptomString = "";
                for(int k=0; k < symptoms.length; k++){
                    symptomString += symptoms[k].getEn();
                    if(k < (symptoms.length-1)){
                        symptomString += ", ";
                    }
                }
                if(r.isAvailable()){
                    if(r.isDiscovered()){
                        item= new com.marmot.intrepid.naturalhealer.data.Item(r.getName(),r.getPicName(),r.getDescription(),r.getProperties(),Double.toString(r.getPrice()),r.getRank().getName().getEn(),"","","","","",1,r.getProtocol(), r.getDifficulty().getEn(),symptomString,1,"recipe");
                    }
                    else {
                        item = new com.marmot.intrepid.naturalhealer.data.Item(r.getName(),r.getPicName(),r.getDescription(),r.getProperties(),Double.toString(r.getPrice()),r.getRank().getName().getEn(),"","","","","",1,r.getProtocol(), r.getDifficulty().getEn(),symptomString,0,"recipe");
                    }
                }
                else {
                    if(r.isDiscovered()){
                        item= new com.marmot.intrepid.naturalhealer.data.Item(r.getName(),r.getPicName(),r.getDescription(),r.getProperties(),Double.toString(r.getPrice()),r.getRank().getName().getEn(),"","","","","",0,r.getProtocol(), r.getDifficulty().getEn(),symptomString,1,"recipe");
                    }
                    else {
                        item= new com.marmot.intrepid.naturalhealer.data.Item(r.getName(),r.getPicName(),r.getDescription(),r.getProperties(),Double.toString(r.getPrice()),r.getRank().getName().getEn(),"","","","","",0,r.getProtocol(), r.getDifficulty().getEn(),symptomString,0,"recipe");
                    }
                }

            }
            else if(i.getClass() == OtherIngredients.class){
                OtherIngredients o = (OtherIngredients) i;
                item= new com.marmot.intrepid.naturalhealer.data.Item(o.getName(),o.getPicName(),o.getDescription(),o.getProperties(),Double.toString(o.getPrice()),o.getRank().getName().getEn(),"","","","","",1,"", "","",0,"other");
            }
            db.itemDAO().insertOrUpdate(item);
        }
    }
}
