package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.Room;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.model.Herb;
import com.marmot.intrepid.naturalhealer.model.OtherIngredients;
import com.marmot.intrepid.naturalhealer.model.Player;
import com.marmot.intrepid.naturalhealer.model.Quest;
import com.marmot.intrepid.naturalhealer.model.Rank;
import com.marmot.intrepid.naturalhealer.model.Recipe;
import com.marmot.intrepid.naturalhealer.model.Villager;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbRarity;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbType;
import com.marmot.intrepid.naturalhealer.model.enumerations.RecipeDifficulty;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;
import com.marmot.intrepid.naturalhealer.service.GameService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseLoad implements Runnable {

    @Override
    public void run() {
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").build();
        GameService game = GameService.getInstance();
        ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items = loadItems(db);
        ArrayList<Player> players = loadPlayers(db, items);
        game.setPlayer(players.get(0));

    }

    public ArrayList<Player> loadPlayers(DAOBase db, ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items){
        //--Loading all players without their respective inventory--
        ArrayList<Player> players = new ArrayList<>();
        for(Player p : db.playerDAO().getAll()){
            players.add(p);
        }

        //--Filling each player's inventory--
        //Getting all players inventory from database
        List<Inventory> inventories = db.inventoryDAO().getAll();
        HashMap<com.marmot.intrepid.naturalhealer.model.Item, Integer> invPlayer = new HashMap<>();

        for(int i=0; i < players.size(); i++){ //Loop on all players
            for(int j=0; j < inventories.size(); j++){ //Loop on all inventories
                if(players.get(i).getNickname() == inventories.get(j).getPlayerName()){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < items.size()){ //Adding each item in player's inventory
                        if(inventories.get(j).getItemName() == items.get(k).getName()){
                            invPlayer.put( items.get(k), new Integer(inventories.get(j).getQuantity()));
                            found = true;
                        }
                    }
                }
            }
            players.get(i).setInventory(invPlayer);
        }
        return players;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.model.Item> loadItems(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.model.Item item = null;
        for(Item it : db.itemDAO().getAll()){
            if(it.getItemType().equals("herb")){
                item = new Herb(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), it.getPrice(), new Rank(it.getRank()), it.getRace(), HerbRarity.findEn(it.getRarity()), it.getHistory(), it.getCombination(), HerbType.findEn(it.getType()));
            }
            else if(it.getItemType().equals("recipe")){
                String[] s = it.getSymptoms().split(",");
                Symptoms[] symptoms = new Symptoms[s.length];
                for(int l=0; l < s.length; l++){
                    symptoms[l] = Symptoms.getEn(s[l]);
                }
                item = new Recipe(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), it.getPrice(), new Rank(it.getRank()), RecipeDifficulty.findEn(it.getDifficulty()), symptoms, it.getProtocol());
            }
            else if(it.getItemType().equals("other")){
                item = new OtherIngredients(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), it.getPrice(), new Rank(it).getRank()));
            }
            items.add(item);
        }

        return items;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.model.Quest> loadQuests(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.model.Quest> quests = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.model.Quest quest = null;
        for(Quest it : db.questDAO().getAll()) {
            quest = new Quest(it.getName(), it.getDescription(), it.getRequirements(), it.getGoals(), it.getRewardMoney(), it.getRewardXp(), it.isCancelable(), it.getType());
            quests.add(quest);
        }
        return quests;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.model.Villager> loadVillagers(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.model.Villager> villagers = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.model.Villager villager = null;
        for(Villager it : db.villagerDAO().getAll()) {
            villager = new Villager(it.getName(), it.getPicName());
            villagers.add(villager);
        }
        return villagers;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.data.QuestList> loadQuestList(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.data.QuestList> questLists = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.data.QuestList questList = null;
        for(QuestList it : db.questListDAO().getAll()) {
            questList = new QuestList(it.getId(), it.getVillagerName(), it.getQuestName());
            questLists.add(questList);
        }
        return questLists;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.data.QuestBook> loadQuestBook(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.data.QuestBook> questBooks = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.data.QuestBook questBook = null;
        for(QuestBook it : db.questBookDAO().getAll()) {
            questBook = new QuestBook(it.getId(), it.getPlayerName(), it.getVillagerName(), it.getQuestName());
            questBooks.add(questBook);
        }
        return questBooks;
    }


}
