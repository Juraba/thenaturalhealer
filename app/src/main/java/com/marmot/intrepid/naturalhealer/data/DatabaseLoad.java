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

        Item h1 = new Item(
                "Thyme",
                "ic_thyme",
                "Plant native to the Mediterranean basin, it is in the form of a sub-shrub of perennial type and particularly dense, with quadrangular and woody stems and sessile leaves. The latter are quite small, lanceolate in shape and gray-green in color. Its size can reach about thirty centimeters and its flower displays a pinkish hue. Small, from 4 to 6 millimeters, it is grouped in leafy spikes and is visible from June to October. The plant prefers fairly rocky, dry and very sunny soils and can grow to altitudes above 1500 meters. Harvesting is usually done at the end of the summer.",
                "In addition to its aromatic use in Provencal cuisine, its various virtues are able to relieve a wide variety of respiratory and intestinal diseases. It is thus a broad-spectrum anti-infective agent and a stimulant of immunity.",
                "0.72",
                "Recruit",
                "Lamiaceae",
                "Present throughout the Mediterranean basin, the plant has been used since ancient times, already in Sumer 5,000 years ago or in Egypt to embalm the dead. It is used in ancient Greece to purify the air. Because of its mythological origins, it also symbolizes courage. It is especially from the Middle Ages that its medicinal virtues begin to be recognized, especially to fight against epidemics, mainly leprosy or plague. From the sixteenth century, it is reported in medical treatises to treat a wide variety of conditions. It was not until the nineteenth century, however, that its active components were listed, the thyme used in the previous century especially for aromatic purposes.",
                "Infusions, essential oils, syrups and tinctures",
                "Common",
                "Aromatic",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h2 = new Item(
                "Basil",
                "ic_basil",
                "Basil or Basil (Ocimum basilicum L.) is a herbaceous herbaceous plant species of the family Lamiaceae (labiaceous, labiate), cultivated as an aromatic and condiment plant. The plant is sometimes called Common Basil, Basil with Sauces, Royal Grass1 or Grand Basilisk. Common Basil is widely used in Italian cuisine, other varieties of basil are prevalent in some Asian cuisines: Taiwan, Thailand, Vietnam, Cambodia and Laos. This plant has had several scientific synonyms including Ocimum basilicum var. Benth glabratum, Ocimum basilicum var. majus Benth.\n" +
                "\n" +
                "\n" +
                "A plant is 20 to 60 cm tall and has oval-lanceolate leaves up to 2-3 cm in size. However, it can easily reach more than one meter in length when it is kept for several years (indoors, or when the climate allows it).\n" +
                "\n" +
                "The leaves are pale green to dark green, sometimes purple purple in some varieties. Stems erect, branched, have a square section like many labiata. They tend to become woody and bushy. The flowers, bilabiate, small and white, have the upper lip divided into four lobes. They are small and grouped in long tubular ears, in the form of elongated clusters. The fine seeds, oblong, are black.",
                "Digestive disorders, sore throat, cough, colds. Basil is a source of vitamin A (antioxidant), vitamin C (tonic) and minerals (phosphorus, calcium, etc.), useful for the development of bone tissue.",
                "0.72",
                "Recruit",
                "Lamiaceae",
                "Basil comes from South Asia6 or Central Africa7. It was imported at least 4,000 years ago in Egypt8. From Egypt, it was imported to Rome and from there to all the south of Europe8 in the second century [ref. necessary]. He would not have reached England before the fourteenth century. He arrived in the Americas with the first emigrants8.\n" +
                        "\n" +
                        "Currently, basil is very widespread around the world. However, it remains deeply associated with Asian and Mediterranean culture and gastronomy.",
                "Sauces, infusions, herbal teas, essential oil",
                "Common",
                "Aromatic",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h3 = new Item(
                "Hypericum / St. John's Wort",
                "ic_hypericum",
                "St. John's wort is a perennial herb up to 90 cm tall (20 to 90 cm) that likes dry, sunny places. The stem is of a reddish color, with oval leaves opposite two by two. What looks like small holes are glandular organs containing the essential oil. The flowers are pentameric (five-petal corolla), of a bright yellow color. They give off a slight fragrance of incense. When one crushes a flower or stalk, a red substance flows. The fruit is a capsule that opens in three parts.",
                "INTERNAL USE\n" +
                        "\n" +
                        "Astringent properties, antiseptics, analgesics, Anti-inflammatories, antispasmodics, sedatives, anxiolytics, antidepressant, antioxidant, antiviral.\n" +
                        "\n" +
                        "EXTERNAL USE\n" +
                        "\n" +
                        "Healing, antiseptic, analgesic, anti-inflammatory.",
                "0.84",
                "Recruit",
                "Clusiaceae or Hypericaceae",
                "In the past, St. John's Wort was known as a \"devil's hunt\" that drove away evil spirits, a plant used in white magic. Nearly 2,500 years ago, St. John's wort was already recommended to treat melancholy, but also to treat wounds, infections or burns.\n" +
                        "\n" +
                        "St. John's wort takes its name from its appearance, because, against the light, the leaves give the impression of being pierced with thousands of holes.",
                "Infusions, mother tinctures, fluid extracts, powders, nebulisates, capsules, essential oils, standardized extracts, poultices, creams.\n" +
                            "\n" +
                            "It is inadvisable to consume St. John's wort along with ginkgo, valerian, hawthorn, passionflower.",
                "Common",
                "Perennial Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h4 = new Item(
                "Absinth",
                "ic_absinth",
                "Perennial, herbaceous plant that measures between 0.50 and 1 meter1. Plant covered with silky silvery white hairs and many oleiferous glands. The stem is silver green in color, straight, fluted, branched and very leafy.\n" +
                        "\n" +
                        "The leaves are alternate, greenish-gray above and almost white and silky on the underside. The basal leaves are up to 25 centimeters long [ref. necessary] and are extensively petiolated. The stem leaves are briefly petiolate, less divided. The leaves at the top can even be simple and sessile (without petiole). The plant has a hard rhizome.\n" +
                        "\n" +
                        "Flowering takes place from July to September. The flowers are yellow, tubular, united in capitules (Compound) globular, bent, in turn united in panicles with leaves and branches. The fruits are achenes.",
                "Absinth is a plant used primarily to fight against stomach upset. In infusion, it also helps treat fatigue or certain disorders such as seasickness. Tonic and aperitif, wormwood is also a dewormer. The medicinal properties of this plant of the Alps are numerous and known since ancient times.",
                "0.72",
                "Recruit",
                "Asteraceae",
                "Used all the time, especially by the Greeks and Romans, the absinthe plant is famous for its medicinal properties. 400 years BC BC, Hippocrates and Pythagoras already extolled the virtues of absinthe alcohol, aphrodisiacs and stimulants for creativity.",
                "Infusions, decoctions, mother tincture",
                "Common",
                "Perennial Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h5 = new Item(
                "Yarrow",
                "ic_yarrow",
                "Yarrow is a perennial plant of the family Asteraceae growing in temperate zones of several continents (Europe, Asia, South America). A plant with a weakly branched stem, it is usually 60 to 80 cm long, although some specimens can reach two meters. The stems are covered with woolly and whitish hairs. The elongated leaves of yarrow are dark green, petiolate at the base. Flowering occurs between June and September: the flower heads of yarrow are white, pink or purple and have white-yellow to yellow florets in their heart.",
                "Dyspepsia, bleeding, treatment of colds ...: yarrow is a plant with multiple medicinal uses. It is used in herbal medicine everywhere on the planet, infusion, mother tincture or external application, to fight against digestive and feminine disorders, loss of appetite, treat wounds or stop bleeding. Its many virtues are due to its 80 or so components and multiple active ingredients.",
                "0.96",
                "Recruit",
                "Asteraceae",
                "Yarrow is named after the Greek hero Achilles who, according to legend, used it to treat wounded soldiers during the Trojan War - the Roman naturalist Pliny gave him his name. However, it seems that its use is much earlier since this plant was already known to Neanderthals. Initially used externally as a healing and to stop bleeding, yarrow has, since the nineteenth century, developed its reputation as an antispasmodic for internal use. During the First World War, yarrow was included in the soldiers' first aid kit.",
                "Infusions, liquid extracts, tinctures, juices, essential oils",
                "Common",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h6 = new Item(
                "Motherwort",
                "ic_motherwort",
                "The motherwort (Leonurus cardiaca) is a perennial of the family Lamiaceae, present throughout Europe, except in the Mediterranean, from 0.50 to 1.20 m, of which leaves and flowers are used.",
                "Plant used for millennia by traditional Chinese medicine as a relaxing, acting on the nervous system and the cardiac system, the motherwort has the first effect of soothing certain disorders such as palpitations, nervousness or anxiety attacks. It is also used to treat high blood pressure and tachycardia.",
                "0.84",
                "Recruit",
                "Lamiaceae",
                "Motherwort comes from the Latin \"acer, acris\", which means \"pointed\" and \"palma\", pointing to the palm of the hand. This refers to the shape of the leaves with acute lobes.",
                "Infusions, decoctions, alcohol tincture, capsules",
                "Common",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h7 = new Item(
                "Garlic",
                "ic_garlic",
                "Garlic is a plant that can reach 50 to 70 cm in height, from the lily family, such as onion, shallot or leek. The bulb is the perennial part that, at the time of flowering, generates a stem with thin, long leaves. In its terminal part, the stem takes the form of an umbrella, made of small white to dark pink flowers. It is a herbaceous plant, whose annual flowering takes place towards the end of summer, in Europe, and all year round in Asian countries. It has a pungent, characteristic odor. The mainly used part is the bulb, consisting of pods surrounded by a very thin sheath. There are different varieties that can be recognized by their color (white, pink or purple).",
                "Renowned since antiquity for its many beneficial effects, garlic can fight against many aches: pains, skin conditions, digestive and respiratory problems, cholesterol or disruption of coagulation and vascular disorders, such as high blood pressure.",
                "0.72",
                "Recruit",
                "Liliaceae ",
                "Although garlic has its origins in Central Asia and was cultivated in Egypt since antiquity, it quickly conquered all continents, first for its medicinal properties and then for its culinary properties. Its use is mentioned in ancient Chinese texts; in Greek medicine, before the Christian era, where it is used as a vermifuge and against asthma; in Arabic medicine, where it is recommended against stomach upset and skin infections. Today, in the West, garlic is also used as a wormer (especially against pinworms), to reduce blood pressure and, locally, to fight against skin infections.",
                "Decoctions, powders, extracts, tablets, capsules, oils, ointments, poultices, plasters.",
                "Common",
                "Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h8 = new Item(
                "Aloe Vera",
                "ic_aloe_vera",
                "A plant of the lily family, such as asparagus, tulip, lily, onion, garlic and chives, Aloe vera is a monocotyledonous plant. The plant is about 70 cm tall, has shallow roots, and is composed of a variable number of fleshy leaves, triangular and pointed at the ends. Small, pale yellow spines are often present around the edges of the leaves. Its light yellow flowering occurs on trumpet-shaped stems and its fruit is capsular. Its gel, a pale green viscous material, is taken from the center of its leaves, while its latex is extracted from the small canals present in its stem. This plant grows on calcareous and sandy soils, in a warm and rather dry atmosphere. It is grown industrially in the United States, the Caribbean, the Philippines and Mexico.",
                "Aloe vera is used in herbal medicine as well as in dermatology or cosmetology. Clinical studies have demonstrated the effectiveness of its therapeutic properties in the treatment of certain dermal conditions, gastrointestinal disorders and it is an excellent antioxidant to fight against cellular aging.",
                "0.72",
                "Recruit",
                "Liliaceae ",
                "Aloe vera originated in Africa and some islands in the Indian Ocean. The gel of this plant was already used to cure skin problems and constipation in ancient Greece and ancient Egypt. For some historians, the Spaniards imported the first plants of aloe in America, around the sixteenth century. In 1820, Aloe vera was mentioned in the official pharmacopoeia of the United States, and in 1935 a group of American doctors used it in the treatment of burns following X-ray exposure. the interest for this plant with one hundred virtues has steadily increased around the world.",
                "Gel, milk, juice, capsules, lotions, creams, drinks",
                "Common",
                "Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h9 = new Item(
                "Anise",
                "ic_anise",
                "Green anise is a herbaceous plant, annual or biennial. It measures between 50 and 80 cm in height. The leaves are green, alternate, long petiolate and composed of three leaflets. The flowers are small and white, grouped in umbels. The fruits are very fragrant seeds, of greenish gray color.",
                "Anise was already used in ancient times for its digestive properties. This plant has soothing effects on the digestive system, but is also effective against coughs or to fight against cold symptoms.",
                "0.96",
                "Recruit",
                "Apiaceae ",
                "This species is native to the eastern Mediterranean basin1. It has spread widely through cultivation in temperate regions.\n" +
                        "\n" +
                        "The green anise is one of the plants whose cultivation is recommended in the royal domains by Charlemagne in the chapter house De Villis (end of the first or beginning of the ninth century).",
                "Infusions, herbal teas, dried seeds, essential oil, mother tincture, micronized powder, decoctions",
                "Common",
                "Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h10 = new Item(
                "Arganier",
                "ic_arganier",
                "Tree endemic to Morocco, with thorny branches, the argan tree can live up to 200 years and reach 10 m in height. Its fruits contain a hard nut, which shelters two or three nuclei from which its oil is drawn.",
                "Argan treats certain skin disorders (irritations, chapped skin, eczema, juvenile acne, mild burns) and softens skin irritated by chickenpox.\n" +
                        "\n" +
                        "Argan oil reduces skin problems (juvenile acne, irritation, eczema); soothes rheumatic pains; reduces cholesterol and blood sugar levels.",
                "1.82",
                "Junior",
                "Sapotaceae ",
                "The argan tree seems to be a relic species. It spread in Morocco during the Tertiary era when the climate was warm and temperate and there was probably a connection between the Moroccan coast and the Canary Islands. It then spread over vast areas, from Morocco to western Algeria.\n" +
                        "\n" +
                        "In the Quaternary, it was reportedly pushed back to the southwest during the ice phase. This would explain the current existence of some settlements in the Rabat region (Khémisset region); in northern Morocco, near the Mediterranean coast in the Beni-Snassen Mountains and north-west of Oujda.\n" +
                        "\n" +
                        "S. Aziki believes that larger and denser argan forests existed in the past but have been degraded by man and his domestic herds.",
                "Combination",
                "Rare",
                "Tree",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h11 = new Item(
                "Arnica",
                "ic_arnica",
                "It is a perennial plant, from a height of 20 to 30 cm. In France, it grows on montane pastures, on siliceous grounds. It supports downy leaves, ovoid and tanned. Their flower heads, 5 or 6 cm wide, are orange-yellow. The base of the flower is a rosette. Its fruit, like the seed, is surmounted by a pappus, with brown or white bristles. The arnica exudes a slight suave fragrance. Harvesting is done at the beginning of flowering, from May to June. Desiccation occurs quickly.",
                "First-line treatment of cutaneous, muscular and articular injuries. Treatment of post-traumatic inflammation and bruising. Treatment of venous pathologies.",
                "1.17",
                "Junior",
                "Asteraceae",
                "Arnica is one of the oldest medicinal plants mentioned by the authors. Descriptions of its therapeutic virtues can be found in the medical treatises of ancient Greece. Thus, Pedanius Dioscorides, doctor and renowned Greek botanist of the first century, called it \"alcimos\", is \"salutary\", and it can be assumed that it was used in prehistory. This popularity, among the therapists, allowed him to be, throughout the history of medicine, the plant most commonly used for the treatment of trauma.",
                "Creams, gels, ointments, ointments, tinctures, massage oils, homeopathic granules",
                "Rare",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h12 = new Item(
                "Bamboo",
                "ic_bamboo",
                "Bamboos (Bambuseae) are a tribe of monocotyledonous plants belonging to the family Poaceae (Graminaceae). They are characterized by stems formed of a lignified hollow stubble with very rapid growth. Bamboos have adapted to many climates (tropical, sub-tropical, and temperate). They are therefore naturally present on all continents with the exception of Europe and Antarctica.\n" +
                        "\n" +
                        "Bamboo has been and remains very widely used as an ornamental plant, food plant and material.",
                "Bamboo is known for its remineralizing and antioxidant properties. It facilitates the reconstruction of cartilage and tissues, including bone. In addition, it promotes the assimilation of phosphorus. Bamboo is used against rheumatism and osteoporosis, but also to strengthen teeth, nails and brittle or fragile hair.",
                "3.95",
                "Apprentice",
                "Poaceae",
                "Some 6,000 years ago, the 竹 (zhu) character for bamboo was engraved on pottery from the Neolithic culture of Yangshao. There are so many uses of bamboo in China in the nineteenth century, the services it renders are so great that it rightly deserves the name of a national tree.",
                "Extracts, infusions, decoctions, oils",
                "Rare",
                "Tree",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h13 = new Item(
                "Burdock",
                "ic_burdock",
                "Burdock is a biennial plant that can reach 2 m in height. Its root is rather fleshy and quite long, since it can measure up to 50 cm. Great burdock has large, alternate, heart-shaped leaves (heart-shaped), hairs on the underside, and a long petiole (between blade and stem). The flowers, purple or purple, are grouped into capitula, gathered in clusters. These flower heads have green bracts, shaped like curved hooks, that help their spread. Burdock can grow up to 1,800 meters above sea level. It is widespread in the temperate regions of America, Asia and Europe (except in the Mediterranean). Burdock grows along roadsides and in abandoned areas.",
                "Burdock is known for its anti-inflammatory and antioxidant properties. It has softening properties and can have a positive effect on itching and inflammation. Renowned for its action on the skin, its root also regulates the secretion of sebum.",
                "0.72",
                "Recruit",
                "Asteraceae",
                "Burdock, native to the temperate regions of Europe, Africa and Asia, has developed in all regions of the world. In traditional Chinese or Indian medicine, this plant was used to treat joint pain, respiratory tract infections and abscesses. Later, during the Middle Ages, Europeans used burdock in a different way: it helped to fight against kidney or bladder problems and acted on skin problems, venereal diseases and cancerous tumors.",
                "Decoctions, poultices, capsules, extracts, ointments, tinctures, powders, infusions",
                "Common",
                "Biennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h14 = new Item(
                "Bergamot",
                "ic_bergamot",
                "Bergamot is the fruit of the bergamot tree, a tree of the family Rutaceae (not to be confused with the bergamot pear or its varieties) mainly grown in the province of Reggio Calabria (Italy) since the eighteenth century and only on a narrow strip of the Calabria coast, along the Ionian Sea and the Tyrrhenian Sea where bergamot still feeds a small local industry.\n" +
                        "\n" +
                        "In 1811, Giorgio Gallesio thought it would come from a cross between a bitter orange and a lime (also called lime).\n" +
                        "\n" +
                        "This fruit weighs between 80 and 200 grams and looks like an orange with greenish flesh and smooth, thick skin, yellow when ripe. Its flesh is slightly acidic and bitter. The zest of the fruit diffuses a powerful and characteristic fragrance that is unique among the citrus fruits, with the only exception, much more discreet, of the lime of Palestine.\n" +
                        "\n" +
                        "Bergamot should not be confused with the \"bergamot lemon tree\", the commercial name of a lemon tree cultivar (Citrus limon) from the Marrakech region.",
                "Properties",
                "0.84",
                "Recruit",
                "Rutaceae",
                "According to some, the bergamot comes from the East and was introduced in Europe by the crusaders and its name would be a distortion of the Turkish word beg armudi which means \"pear of the lord\". Others maintain that it was brought back from the Canary Islands by Christopher Columbus and that it would take its name from the city of Berga, north of Barcelona, where it was originally cultivated.\n" +
                        "\n" +
                        "According to legend, on a full moon night, bergamot shows us where the fairies are born.\n" +
                        "\n" +
                        "The high density of plantation in Calabria, long occupied by the Arabs, suggests that it is a varietal selection typical of Iberian-Moorish agronomists of the Middle Ages (9th-11th centuries), its use for its essence reminds the citrus fruits which had their favor: bigarade, citron, etc. There is a local legend that the bergamot was exchanged between a Christian and an Arab for 12 shields, each party to exchange considered to have made an excellent deal.",
                "Infusions, essential oil, cosmetics",
                "Common",
                "Fruit",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h15 = new Item(
                "Calendula",
                "ic_calendula",
                "Calendula officinalis has an elongated downy stem, ending in a very bright, orange-yellow flower that is reminiscent of a small sun. It blooms from spring until the first frosts. Easy to live and resembling itself, it supports ordinary soils and flourishes in fields and gardens, where it is just as much appreciated as an ornamental plant as an auxiliary against aphids. Some cooks do not hesitate to incorporate it into their recipes, since its flower is edible.",
                "Plant of predilection of phytotherapy, recognized for its calming and healing virtues, the marigold is a sovereign remedy for the small cutaneous problems. It also relieves gastric disorders, liver diseases, inflammation of the throat and mouth or premenstrual pain.",
                "1.64",
                "Junior",
                "Asteraceae",
                "Still known as \"the fiancée of the sun\", the care of the gardens, whose flower has the particularity of opening and closing according to the movements of the sun, naturally finds its origin in the heart of the Mediterranean basin. Not very demanding, the plant quickly spread to Europe, where it has been cultivated since the twelfth century, and now thrives in all regions of the world that enjoy a temperate climate. Already known in ancient times for its culinary, cosmetic, medicinal and dyeing virtues (dyeing for fabrics and food coloring), this plant took off from the Middle Ages. Calendula officinalis was then known to soothe all kinds of ailments (insect bites, snake bites, jaundice, conjunctivitis, ringworm, painful periods, fever). Today, it is a remedy especially appreciated in dermatological, for its softening and healing properties.",
                "Ointments, creams, gels, balms, mother tinctures, oils, eye drops, vaginal eggs, infusions",
                "Rare",
                "Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h16 = new Item(
                "Chamomile",
                "ic_chamomile",
                "Roman chamomile is a perennial herb that can reach 10 to 30 cm tall. It is whitish, its stems are lying or erect. Its alternate leaves are filiform, divided into lobes. Its flower heads are 20 to 30 mm in diameter. The flowers are yellow and white, erected alone, at the end of a stem. Its scent is bitter and its aromatic smell. The fruits are yellow, ribbed achenes. It flowers from June to September. Very widespread in temperate regions, it does not like altitude. It is particularly present in Anjou, where siliceous grounds suit it.",
                "Dyspepsia of gastric and hepatic origins; inflammations of the digestive tract (esophagitis, gastritis, ulcers); digestive and hepatic migraines; dental neuralgia; anorexic tendencies; flus and digestive fevers; engorgement of the liver or spleen. Inflammatory dermatoses; burns; eczema; vulnerable pruritus; neuralgic pains. Roman chamomile has a positive effect on the central nervous system; it plays a sedative, calming role. It has an antiviral action.",
                "1.44",
                "Junior",
                "Asteraceae",
                "Roman chamomile was already used in antiquity. Pharaoh Ramses II was allegedly embalmed with the essential oil of Roman chamomile. It also helped to reduce fever and treat sores, body aches or female disorders. By the sixth century, this plant was used to reduce insomnia, back pain and symptoms of indigestion. She participates in the development of many pharmaceutical preparations. In the sixteenth and seventeenth centuries, the Roman chamomile was very present in Rome, city from which it takes its name.",
                "Combination",
                "Common",
                "Perennial Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
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
        Item h53 = new Item("Lemon", "ic_lemon", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");

        Item r1 = new Item("Herbal Brew", "ic_herbal_brew", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "recipe");
        Item r2 = new Item("Ointment", "ic_ointment", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");
        Item r3 = new Item("Soup", "ic_soup", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");
        Item r4= new Item("Cake", "ic_cake", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 0, "Protocol", "Easy", "Cough, Headache, Runny nose", 1, "recipe");

        Item o1 = new Item("Water", "ic_water", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");
        Item o2 = new Item("Milk", "ic_milk", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");
        Item o3 = new Item("Rhum", "ic_rhum", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");
        Item o4 = new Item("Sugar", "ic_sugar", "Description", "Properties", "10.00","Recruit", "", "", "", "", "", 1, "Protocol", "Easy", "Nervous, Muscle Pain, Stressed, Tensed, Tired", 1, "other");

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
        db.itemDAO().insertItem(h53);

        db.itemDAO().insertItem(r1);
        db.itemDAO().insertItem(r2);
        db.itemDAO().insertItem(r3);
        db.itemDAO().insertItem(r4);

        db.itemDAO().insertItem(o1);
        db.itemDAO().insertItem(o2);
        db.itemDAO().insertItem(o3);
        db.itemDAO().insertItem(o4);
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
