package com.marmot.intrepid.naturalhealer.data;


import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Villager;


public class DatabaseInit implements Runnable {
    public void run(){
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").build();
        if(db.playerDAO().getAll() == null || db.playerDAO().getAll().size() == 0){
            System.out.println("Database empty -- Initialization");
            initPlayer(db);
            initItem(db);
            initVillager(db);
            initQuest(db);
            initQuestBook(db);
            initQuestList(db);
            initInventory(db);
        }
        else {
            System.out.println("Database not empty -- No need to initialize");
        }
    }

    public void initPlayer(DAOBase db){
        Player player = new Player("Jean-Michel Druide", "ic_player", 930, "500.00", "Recruit");
        db.playerDAO().insertOne(player);
    }

    public void initItem(DAOBase db){

        Item h1 = new Item("Thym", "ic_herb_thyme", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Common", 0, "", "", "", 0, "herb");
        Item h2 = new Item("Basilic", "ic_herb_basil", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Common", 0, "", "", "", 0, "herb");
        Item h3 = new Item("Millepertuis", "ic_herb_st_johns_wort", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Common", 0, "", "", "", 0, "herb");

        Item r1 = new Item("Tisane", "ic_recipe_herbal_brew", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");
        Item r2 = new Item("Onguent", "ic_recipe_ointment", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");

        Item o1 = new Item("Water", "ic_other_water", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");
        Item o2 = new Item("Milk", "ic_other_milk", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");
        Item o3 = new Item("Rhum", "ic_other_rhum", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");

        db.itemDAO().insertItem(h1);
        db.itemDAO().insertItem(h2);
        db.itemDAO().insertItem(h3);

        db.itemDAO().insertItem(r1);
        db.itemDAO().insertItem(r2);

        db.itemDAO().insertItem(o1);
        db.itemDAO().insertItem(o2);
        db.itemDAO().insertItem(o3);
    }

    public void initVillager(DAOBase db){
        Villager v1 = new Villager("Mme.SEGUIN", "seguin.png");
        Villager v2 = new Villager("M.LE MAIRE", "lemaire.png");
        Villager v3 = new Villager("Mme.ROSSIGNOL", "rossignol.png");
        Villager v4 = new Villager("M.BROSSARD", "brossard.png");
        Villager v5 = new Villager("M.RABAULT", "rabault.png");

        db.villagerDAO().insertVillager(v1);
        db.villagerDAO().insertVillager(v2);
        db.villagerDAO().insertVillager(v3);
        db.villagerDAO().insertVillager(v4);
        db.villagerDAO().insertVillager(v5);
    }

    public void initQuest(DAOBase db){
        Quest q1 = new Quest("Douleurs musculaires", "Madame SEGUIN a des douleurs musculaires dues à sa vieillesse, trouvez de quoi la soulager !", "Main",500, 10, "true", "");
        Quest q2 = new Quest("Stress intense", "Monsieur LE MAIRE est très stressé à cause de son travail, aidez-le à retrouver sa sérénité", "Daily", 500, 10, "true", "Daily");
        Quest q3 = new Quest("Sommeil fuyard", "Monsieur LE MAIRE a du mal à trouver le sommeil ces derniers temps, auriez-vous de quoi l'apaiser ?", "Main",500,10,"true", "Main");
        Quest q4 = new Quest("Mal de tête", "Madame ROSSIGNOL subit un affreux mal de tête au travail depuis quelques temps, essayez de stopper cet enfer !", "Main", 500, 10, "true", "Main");

        db.questDAO().insertQuest(q1);
        db.questDAO().insertQuest(q2);
        db.questDAO().insertQuest(q3);
        db.questDAO().insertQuest(q4);
    }

    public void initQuestBook(DAOBase db){
        QuestBook q1 = new QuestBook(0, "Jean-Michel Druide", "M.LE MAIRE", "Mal de tête");
        QuestBook q2 = new QuestBook(0, "Jean-Michel Druide", "Mme. ROSSIGNOL", "Stress intense");

        db.questBookDAO().insertQuestBook(q1);
        db.questBookDAO().insertQuestBook(q2);
    }

    public void initQuestList(DAOBase db){
        QuestList q1 = new QuestList(0, "M.LE MAIRE", "Mal de tête");
        QuestList q2 = new QuestList(0, "Mme. ROSSIGNOL", "Stress intense");
        QuestList q3 = new QuestList(0, "M.LE MAIRE", "Douleurs musculaires");
        QuestList q4 = new QuestList(0, "M.BROSSARD", "Sommeil fuyard");

        db.questListDAO().insertQuestList(q1);
        db.questListDAO().insertQuestList(q2);
        db.questListDAO().insertQuestList(q3);
        db.questListDAO().insertQuestList(q4);
    }

    public void initInventory(DAOBase db){
        Inventory it1 = new Inventory(0, "Jean-Michel Druide", "Basilic", "herb", 8);
        Inventory it2 = new Inventory(0, "Jean-Michel Druide", "Water", "other", 17);
        Inventory it3 = new Inventory(0, "Jean-Michel Druide", "Onguent", "recipe", 3);

        db.inventoryDAO().insertInventory(it1);
        db.inventoryDAO().insertInventory(it2);
        db.inventoryDAO().insertInventory(it3);
    }
}
