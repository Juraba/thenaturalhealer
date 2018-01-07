package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;

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
        DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        if(db.playerDAO().getAll() == null || db.playerDAO().getAll().size() == 0){
            System.out.println("Database empty -- Initialization");
            System.out.println("Initialization Player");
            initPlayer(db);
            System.out.println("Initialization Item");
            initItem(db);
            System.out.println("Initialization Villager");
            initVillager(db);
            System.out.println("Initialization Quest");
            initQuest(db);
            System.out.println("Initialization QuestBook");
            initQuestBook(db);
            System.out.println("Initialization QuestList");
            initQuestList(db);
            System.out.println("Initialization Inventory");
            initInventory(db);
            System.out.println("Initialization Requirements");
            initRequirements(db);
            System.out.println("Initialization ended");
        }
        else {
            System.out.println("Database not empty -- No need to initialize");
        }

        System.out.println("=== Database Load ===");
        System.out.println("--Getting database and game instance--");
        //Getting database instance
        //DAOBase db = Room.databaseBuilder(MainActivity.getContext(), DAOBase.class, "db-thenaturalhealer").addMigrations(MainActivity.getMigration12()).build();
        //Getting game instance
        GameService game = GameService.getInstance();

        System.out.println("--Loading data--");
        //Loading game data from database
        ArrayList<com.marmot.intrepid.naturalhealer.model.Item> items = loadItems(db);
        ArrayList<Quest> quests = loadQuests(db, items);
        ArrayList<Villager> villagers = loadVillagers(db, quests);
        ArrayList<Player> players = loadPlayers(db, items, quests);

        System.out.println("--Setting up game parameters--");
        //Setting up the data into the game's parameters
        game.setPlayer(players.get(0));
        game.setItems(items);
        game.setVillagers(villagers);
        game.setGrimoire(items);
        System.out.println(players.get(0).getNickname());
        System.out.println(players.get(0).getInventory());
        game.setShop(items);

        System.out.println("--End of loading--");
        db.close();

    }

    //Loading

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
                if(players.get(i).getNickname().equals(inventories.get(j).getPlayerName())){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < items.size()){ //Adding each item in player's inventory
                        if(inventories.get(j).getItemName().equals(items.get(k).getName())){
                            invPlayer.put( items.get(k), inventories.get(j).getQuantity());
                            found = true;
                        }
                        k++;
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
                if(players.get(i).getNickname().equals(questbooks.get(j).getPlayerName())){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < quests.size()){ //Adding each item in player's inventory
                        if(questbooks.get(j).getQuestName().equals(quests.get(k).getName())){
                            qbPlayer.put( questbooks.get(j).getVillagerName(), quests.get(k));
                            found = true;
                        }
                        k++;
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
                String[] s = it.getSymptoms().split(", ");
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
                if(quests.get(i).getName().equals(requirements.get(j).getQuestName())){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < items.size()){ //Adding each item in player's inventory
                        if(requirements.get(j).getItemName().equals(items.get(k).getName())){
                            requirementsList.put(items.get(k), new Integer(requirements.get(j).getQuantity()));
                            found = true;
                        }
                        k++;
                    }

                }
            }
            quests.get(i).setRequirements(requirementsList);
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
                if(villagers.get(i).getName().equals(questlists.get(j).getVillagerName())){
                    int k = 0;
                    boolean found = false;
                    while(!found && k < quests.size()){ //Adding each item in player's inventory
                        if(questlists.get(j).getQuestName().equals(quests.get(k).getName())){
                            villagers.get(i).addQuest(quests.get(k));
                            found = true;
                        }
                        k++;
                    }

                }
            }
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

    //Initialization

    public void initPlayer(DAOBase db){
        Player player = new Player("Jean-Michel Druide", "ic_player", 930, "500.00", "Recruit");
        db.playerDAO().insertOne(player);
    }

    public void initItem(DAOBase db){

        Item h1 = new Item("Thyme", "ic_thyme", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h2 = new Item("Basil", "ic_basil", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h3 = new Item("Hypericum", "ic_hypericum", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h4 = new Item("Absinth", "ic_absinth", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h5 = new Item("Yarrow", "ic_yarrow", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h6 = new Item("Motherwort", "ic_motherwort", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h7 = new Item("Garbic", "ic_garbic", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h8 = new Item("Aloe Vera", "ic_aloe_vera", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h9 = new Item("Anise", "ic_anise", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h10 = new Item("Arganier", "ic_arganier", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h11 = new Item("Arnica", "ic_arnica", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h12 = new Item("Bamboo", "ic_bamboo", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h13 = new Item("Burdock", "ic_burdock", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h14 = new Item("Bergamot", "ic_bergamot", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h15 = new Item("Calendula", "ic_calendula", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h16 = new Item("Chamomile", "ic_chamomile", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h17 = new Item("Cinnamon", "ic_cinnamon", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h18 = new Item("Cardamom", "ic_cardamom", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h19 = new Item("Chervil", "ic_chervil", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h20 = new Item("Vegetable Coal", "ic_vegetable_coal", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h21 = new Item("Chicory", "ic_chicory", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h22 = new Item("Citronella", "ic_citronella", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h23 = new Item("Coriander", "ic_coriander", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h24 = new Item("Comfrey", "ic_comfrey", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h25 = new Item("Tarragon", "ic_tarragon", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h26 = new Item("Eucalyptus", "ic_eucalyptus", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h27 = new Item("Fennel", "ic_fennel", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h28 = new Item("Fenugreek", "ic_fenugreek", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h29 = new Item("Galanga", "ic_galanga", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h30 = new Item("Gentian", "ic_gentian", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h31 = new Item("Ginger", "ic_ginger", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h32 = new Item("Ginseng", "ic_ginseng", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h33 = new Item("Jasmine", "ic_jasmine", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h34 = new Item("Lavender", "ic_lavender", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h35 = new Item("Marjoram", "ic_marjoram", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h36 = new Item("Primrose", "ic_primrose", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h37 = new Item("Nettle", "ic_nettle", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h38 = new Item("Passiflora", "ic_passiflora", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h39 = new Item("Salad Burnet", "ic_salad_burnet", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h40 = new Item("Rosemary", "ic_rosemary", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h41 = new Item("Rooibos", "ic_rooibos", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h42 = new Item("Rose", "ic_rose", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h43 = new Item("Saffron", "ic_saffron", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h44 = new Item("Sage", "ic_sage", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h45 = new Item("Elderberry", "ic_elderberry", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h46 = new Item("Green Tea", "ic_green_tea", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h47 = new Item("Linden", "ic_linden", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h48 = new Item("Red Clover", "ic_red_clover", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h49 = new Item("Valerian", "ic_valerian", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h50 = new Item("Vanilla", "ic_vanilla", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h51 = new Item("Verbena", "ic_verbena", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h52 = new Item("Mint", "ic_mint", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");

        Item r1 = new Item("Herbal Brew", "ic_herbal_brew", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");
        Item r2 = new Item("Ointment", "ic_ointment", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");
        Item r3 = new Item("Soup", "ic_soup", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");
        Item r4= new Item("Cake", "ic_cake", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");

        Item o1 = new Item("Water", "ic_water", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");
        Item o2 = new Item("Milk", "ic_milk", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");
        Item o3 = new Item("Rhum", "ic_rhum", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");

        db.itemDAO().insertItem(h1);
        db.itemDAO().insertItem(h2);
        db.itemDAO().insertItem(h3);
        db.itemDAO().insertItem(h4);
        db.itemDAO().insertItem(h5);
        db.itemDAO().insertItem(h6);
        db.itemDAO().insertItem(h7);
        db.itemDAO().insertItem(h8);
        db.itemDAO().insertItem(h9);
        db.itemDAO().insertItem(h10);
        db.itemDAO().insertItem(h11);
        db.itemDAO().insertItem(h12);
        db.itemDAO().insertItem(h13);
        db.itemDAO().insertItem(h14);
        db.itemDAO().insertItem(h15);
        db.itemDAO().insertItem(h16);
        db.itemDAO().insertItem(h17);
        db.itemDAO().insertItem(h18);
        db.itemDAO().insertItem(h19);
        db.itemDAO().insertItem(h20);
        db.itemDAO().insertItem(h21);
        db.itemDAO().insertItem(h22);
        db.itemDAO().insertItem(h23);
        db.itemDAO().insertItem(h24);
        db.itemDAO().insertItem(h25);
        db.itemDAO().insertItem(h26);
        db.itemDAO().insertItem(h27);
        db.itemDAO().insertItem(h28);
        db.itemDAO().insertItem(h29);
        db.itemDAO().insertItem(h30);
        db.itemDAO().insertItem(h31);
        db.itemDAO().insertItem(h32);
        db.itemDAO().insertItem(h33);
        db.itemDAO().insertItem(h34);
        db.itemDAO().insertItem(h35);
        db.itemDAO().insertItem(h36);
        db.itemDAO().insertItem(h37);
        db.itemDAO().insertItem(h38);
        db.itemDAO().insertItem(h39);
        db.itemDAO().insertItem(h40);
        db.itemDAO().insertItem(h41);
        db.itemDAO().insertItem(h42);
        db.itemDAO().insertItem(h43);
        db.itemDAO().insertItem(h44);
        db.itemDAO().insertItem(h45);
        db.itemDAO().insertItem(h46);
        db.itemDAO().insertItem(h47);
        db.itemDAO().insertItem(h48);
        db.itemDAO().insertItem(h49);
        db.itemDAO().insertItem(h50);
        db.itemDAO().insertItem(h51);
        db.itemDAO().insertItem(h52);

        db.itemDAO().insertItem(r1);
        db.itemDAO().insertItem(r2);
        db.itemDAO().insertItem(r3);
        db.itemDAO().insertItem(r4);

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
        QuestBook q2 = new QuestBook(0, "Jean-Michel Druide", "Mme.ROSSIGNOL", "Stress intense");

        db.questBookDAO().insertQuestBook(q1);
        db.questBookDAO().insertQuestBook(q2);
    }

    public void initQuestList(DAOBase db){
        QuestList q1 = new QuestList(0, "M.LE MAIRE", "Stress intense");
        QuestList q2 = new QuestList(0, "Mme.ROSSIGNOL", "Mal de tête");
        QuestList q3 = new QuestList(0, "M.LE MAIRE", "Sommeil fuyard");
        QuestList q4 = new QuestList(0, "M.BROSSARD", "Douleurs musculaires");

        db.questListDAO().insertQuestList(q1);
        db.questListDAO().insertQuestList(q2);
        db.questListDAO().insertQuestList(q3);
        db.questListDAO().insertQuestList(q4);
    }

    public void initInventory(DAOBase db){
        Inventory it1 = new Inventory(0, "Jean-Michel Druide", "Basil", "herb", 8);
        Inventory it2 = new Inventory(0, "Jean-Michel Druide", "Water", "other", 17);
        Inventory it3 = new Inventory(0, "Jean-Michel Druide", "Ointment", "recipe", 3);

        db.inventoryDAO().insertInventory(it1);
        db.inventoryDAO().insertInventory(it2);
        db.inventoryDAO().insertInventory(it3);
    }

    public void initRequirements(DAOBase db){
        Requirements r1 = new Requirements(0, "Douleurs musculaires","Water", 15);
        Requirements r2 = new Requirements(0, "Douleurs musculaires","Basil", 4);
        Requirements r3 = new Requirements(0, "Mal de tête","Soup", 2);

        db.requirementsDAO().insertRequirements(r1);
        db.requirementsDAO().insertRequirements(r2);
        db.requirementsDAO().insertRequirements(r3);
    }
}
