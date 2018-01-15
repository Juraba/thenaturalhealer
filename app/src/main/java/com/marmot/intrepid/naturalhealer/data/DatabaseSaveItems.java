package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;

public class DatabaseSaveItems implements Runnable {

    @Override
    public void run() {
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        //Getting game instance
        GameService game = GameService.getInstance();
        saveItem(db, game.getItems());
        db.close();
    }

    public void saveItem(DAOBase db, ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items){
        for(com.marmot.intrepid.naturalhealer.model.Item i : items){
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
