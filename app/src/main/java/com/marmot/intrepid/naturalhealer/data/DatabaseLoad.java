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
        //Getting database instance
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").build();
        //Getting game instance
        GameService game = GameService.getInstance();

        //Loading game data from database
        ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items = loadItems(db);
        ArrayList<Quest> quests = loadQuests(db, items);
        ArrayList<Villager> villagers = loadVillagers(db, quests);
        ArrayList<Player> players = loadPlayers(db, items, quests);


        //Setting up the data into the game's parameters
        game.setPlayer(players.get(0));
        game.setItems(items);
        game.setVillagers(villagers);
        game.setGrimoire(items);
        game.setShop(items);

    }

    public ArrayList<Player> loadPlayers(DAOBase db, ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items, ArrayList<Quest> quests){
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

        //--Loading players questbook--
        List<QuestBook> questbooks = db.questBookDAO().getAll();
        HashMap<String, Quest> qbPlayer = new HashMap<>();
        for(int i=0; i < players.size(); i++){ //Loop on all players
            for(int j=0; j < questbooks.size(); j++){ //Loop on all questbooks
                if(players.get(i).getNickname() == questbooks.get(j).getPlayerName()){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < quests.size()){ //Adding each item in player's inventory
                        if(questbooks.get(j).getQuestName() == quests.get(k).getName()){
                            qbPlayer.put( quests.get(k).getName(), quests.get(k));
                            found = true;
                        }
                    }
                }
            }
            players.get(i).setQuests(qbPlayer);
        }

        return players;
    }

    public ArrayList<com.marmot.intrepid.naturalhealer.model.Item> loadItems(DAOBase db){
        ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items = new ArrayList<>();
        com.marmot.intrepid.naturalhealer.model.Item item = null;
        for(Item it : db.itemDAO().getAll()){
            if(it.getItemType().equals("herb")){
                item = new Herb(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), Double.parseDouble(it.getPrice()), new Rank(it.getRank()), it.getRace(), HerbRarity.findEn(it.getRarity()), it.getHistory(), it.getCombination(), HerbType.findEn(it.getType()));
            }
            else if(it.getItemType().equals("recipe")){
                String[] s = it.getSymptoms().split(",");
                Symptoms[] symptoms = new Symptoms[s.length];
                for(int l=0; l < s.length; l++){
                    symptoms[l] = Symptoms.findEn(s[l]);
                }
                item = new Recipe(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), Double.parseDouble(it.getPrice()), new Rank(it.getRank()), RecipeDifficulty.findEn(it.getDifficulty()), symptoms, it.getProtocol());
            }
            else if(it.getItemType().equals("other")){
                item = new OtherIngredients(it.getName(), it.getPicName(), it.getDescription(), it.getProperties(), Double.parseDouble(it.getPrice()), new Rank(it.getRank()));
            }
            items.add(item);
        }

        return items;
    }

    public ArrayList<Quest> loadQuests(DAOBase db, ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items){
        ArrayList<Quest> quests = new ArrayList<>();
        Quest quest = null;
        for(Quest it : db.questDAO().getAll()) {
            quest = new Quest(it.getName(), it.getDescription(), it.getRequirements(), it.getRewardMoney(), it.getRewardXp(), it.isCancelable(), it.getType());
            quests.add(quest);
        }

        List<Requirements> requirements = db.requirementsDAO().getAll();
        HashMap<com.marmot.intrepid.naturalhealer.model.Item, Integer> requirementsList = new HashMap<>();
        for(int i=0; i < quests.size(); i++){
            for(int j=0; j < requirements.size(); j++){
                if(quests.get(i).getName() == requirements.get(j).getQuestName()){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < items.size()){ //Adding each item in player's inventory
                        if(requirements.get(j).getItemName() == items.get(k).getName()){
                            requirementsList.put(items.get(k), new Integer(requirements.get(j).getQuantity()));
                            found = true;
                        }
                    }

                }
            }
        }

        return quests;
    }

    public ArrayList<Villager> loadVillagers(DAOBase db, ArrayList<Quest> quests){
        ArrayList<Villager> villagers = new ArrayList<>();
        Villager villager;
        for(Villager it : db.villagerDAO().getAll()) {
            villager = new Villager(it.getName(), it.getPicName());
            villagers.add(villager);
        }

        //--Loading villagers questlist--
        List<QuestList> questlists = db.questListDAO().getAll();
        for(int i=0; i < villagers.size(); i++){ //Loop on all players
            for(int j=0; j < questlists.size(); j++){ //Loop on all questbooks
                if(villagers.get(i).getName() == questlists.get(j).getVillagerName()){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < quests.size()){ //Adding each item in player's inventory
                        if(questlists.get(j).getQuestName() == quests.get(k).getName()){
                            villagers.get(i).addQuest(quests.get(k));
                            found = true;
                        }
                    }

                }
            }
        }

        return villagers;
    }
}
