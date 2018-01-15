package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.*;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseSaveVillager implements Runnable {

    @Override
    public void run() {
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        //Getting game instance
        GameService game = GameService.getInstance();
        saveVillager(db, game.getVillagers());
        db.close();
    }

    public void saveVillager(DAOBase db, ArrayList<Villager> villagers){
        for(Villager v : villagers){
            db.villagerDAO().insertOrUpdate(v);
            ArrayList<Quest> vQuests = v.getQuests();
            for(Quest q : vQuests){
                QuestList ql = new QuestList(0,v.getName(), q.getName());
                db.questListDAO().insertOrUpdate(ql);
                db.questDAO().insertOrUpdate(q);

                HashMap<com.marmot.intrepid.naturalhealer.model.Item, Integer> requirements = q.getRequirements();
                for(HashMap.Entry<com.marmot.intrepid.naturalhealer.model.Item, Integer> r : requirements.entrySet()){
                    Requirements requirement = new Requirements(0, q.getName(), r.getKey().getName(), r.getValue());
                    db.requirementsDAO().insertOrUpdate(requirement);
                }
            }
        }
    }
}
