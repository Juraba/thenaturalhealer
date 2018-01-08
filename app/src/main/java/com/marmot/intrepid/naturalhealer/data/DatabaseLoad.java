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
                "Perennial, herbaceous plant that measures between 0.50 and 1 meter. Plant covered with silky silvery white hairs and many oleiferous glands. The stem is silver green in color, straight, fluted, branched and very leafy.\n" +
                        "\n" +
                        "The leaves are alternate, greenish-gray above and almost white and silky on the underside. The basal leaves are up to 25 centimeters long and are extensively petiolated. The stem leaves are briefly petiolate, less divided. The leaves at the top can even be simple and sessile (without petiole). The plant has a hard rhizome.\n" +
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
        Item h17 = new Item(
                "Cinnamon",
                "ic_cinnamon",
                "Growing from seedlings or cuttings, the cinnamon tree can reach 15 m high. Depending on its species, it becomes tree or remains shrub. It can not withstand temperatures below 15 ° C and only lives in tropical or subtropical regions. Growing on light soil, it is harvested for the first time only six or seven years after planting. The branches are then cut for the harvest of the bark, which will be dried. Starch and proanthocyanidolic oligomers are recovered.",
                "Intestinal transit, dental pain, diabetes, fatigue. Prevents the progression of Alzheimer's disease, promotes weight loss, reduces aging due to stress, stimulates the immune system, has a disinfecting power of parts by diffusion.",
                "1.08",
                "Recruit",
                "Lauraceae",
                "Originally from Sri Lanka, cinnamon is better known as \"cinnamon of Ceylon\". Used for more than 5,000 years, it is Ayurvedic medicine (India), then Chinese medicine that used it first, in combination with turmeric, ginger, nutmeg, clove or cardamom. Fragrant wine, cinnamon is also incorporated into many dishes, whether sweet or savory. Over time, it has been found that cinnamon has medical \"powers\", such as those to fight against winter infections (colds or flus). Its antimicrobial strengths and its glycemic enhancing properties were brought to light. Grown mainly in China, Vietnam, Indonesia, Madagascar and Sri Lanka, this plant was very expensive. Nowadays, it is accessible to everyone and is used for its aromatic benefits, as well as in the composition of some dental products and cosmetics.",
                "Sticks, essential oils, ground powder, herbal teas, tinctures, capsules, tablets, dental rinse solutions",
                "Common",
                "Tree",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h18 = new Item(
                "Cardamom",
                "ic_cardamom",
                "Cardamom is a plant in the zingiberaceae family, as are ginger and turmeric. It is a perennial herb with a rhizome that can reach a height of 2 to 5 m.",
                "Cardamom effectively treats many digestive disorders such as indigestion, colic, gastritis and gastralgia, diarrhea, functional colopathy, Crohn's disease and digestive spasms. The essential oil of cardamom is recognized for its anti-inflammatory, antispasmodic and analgesic properties.",
                "1.56",
                "Junior",
                "Zingiberaceae",
                "Cardamom has been used for millennia in Ayurvedic medicine to treat digestive disorders, certain respiratory disorders (such as asthma and bronchitis), anorexia, anemia, asthenia and kidney stones. It was also known as an aphrodisiac and is still used against bad breath. It could also be used in case of snake or scorpion bite. It arrived in Western Europe from the fifteenth century on the back of trade with the Indian subcontinent, which began to develop at that time. It was also known in ancient Greece, as early as the fourth century BCE. His presence in Egypt has been attested for at least twenty-five centuries. It has also been used in China for a long time to treat urinary disorders.",
                "Essential oils, tinctures, infusions, powders, capsules, poultices, decoctions",
                "Rare",
                "Perennial Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h19 = new Item(
                "Chervil",
                "ic_chervil",
                "A biennial herbaceous plant, grown as an annual, 30 to 60 cm tall. Leaves long petiole, very tender and finely divided. Inflorescence in umbels of white flowers.\n" +
                        "\n" +
                        "This plant, which resembles parsley, presents a risk of confusion with the little hemlock, (Aethusa cynapium), a toxic plant, which does not have the same pleasant smell.",
                "Chervil can be eaten in infusions, to promote digestion. While it helps to fight against the retention of urine, it also acts on gout, asthma, bronchitis and kidney stones.",
                "0.93",
                "Recruit",
                "Apiaceae",
                "It is one of the plants whose cultivation is recommended in the royal domains by Charlemagne in the chapter house De Villis (end of the first or beginning of the ninth century).",
                "Poultices, decoctions, infusions, mother tincture, fresh juice, fresh leaves",
                "Common",
                "Biennial Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h20 = new Item(
                "Vegetable Coal",
                "ic_vegetable_coal",
                "In France, charcoal was made from birch, oak, beech, poplar, pine, willow or linden.\n" +
                        "\n" +
                        "Poplar coal, also known as \"Belloc coal\", is prepared from tree shoots of three to four years. The shoots are calcined in a vacuum. The coal is boiled in hydrochloric acid diluted 1/32. It is then washed, dried and pulverized. It is kept away from the air to prevent it from absorbing atmospheric gases and moisture.",
                "Occasional constipation, diarrhea, gastroenteritis, heartburn, bad breath, flatulence, irritable bowel syndrome, intoxication or poisoning.",
                "1.08",
                "Recruit",
                "Carbo vegetabilis",
                "Evidence of the use of activated charcoal dates back to antiquity with medicinal uses by Hippocrates around 400 BC. AD or for purification of water by the Egyptians towards 1 550 BC. In the eighteenth century, animal black, produced from bone, was used for the purification of liquids by filtration and for discoloration, especially for the production of white sugar. Scottish John Stenhouse used it for the first breathing masks (1860, 1867).\n" +
                        "\n" +
                        "It was in the twentieth century that production processes were improved to allow industrial production of activated carbons for various applications: capture of pollutant in gaseous or aqueous phase, gas separation processes, etc. Physical or chemical treatment processes have been developed to enable the production of active carbons with greater efficiency: the Swedish chemist von Ostreijko defined the basics of physical (carbon dioxide water vapor) and chemical (chlorides) activation two patents dated 1900 and 1901, and chemical activation by acids (Bayer, 1905), etc.",
                "Powders, capsules, granules.",
                "Common",
                "Material",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h21 = new Item(
                "Chicory",
                "ic_chicory",
                "Herbaceous with a taproot, wild chicory comes from Europe, North Africa and Asia. Measuring 40 cm to 1 m, this plant has basal, intermediate and upper leaves of elongated shape on an angular stem. The flowers of chicory are in capitula and are heliotropic, they change from blue to pale blue and pink depending on the hours of the day. Similarly, they open under the sun and close in overcast weather or at night, which is important for gathering for medicinal purposes. Several varieties of chicory are also grown for food purposes. In the wild, chicory grows easily in meadows, ditches and on the edges of fields.",
                "Chicory is useful in herbal medicine, in the treatment of gastric and intestinal disorders, pain, nausea, indigestion, etc. It also helps fight against lack of appetite. It promotes the proper functioning of the gastrointestinal system.",
                "0.72",
                "Recruit",
                "Asteraceae",
                "\"The humblest plants are sometimes the richest in hidden virtues,\" wrote Maurice Mességué, a passionate herbalist, about chicory. Our ancestors were not mistaken, who already used this plant 4,000 years before Christ, mainly for digestive purposes. Chicory has had other uses since, in the Middle Ages, it was considered an anaphrodisiac plant: its roots and crushed leaves then ingested were supposed to calm all the heat! Beyond this particular property, chicory roots were also used to fight against many diseases, including jaundice, angina pectoris and congestions of the liver. The discovery of the process of roasting in the seventeenth century and the blockade of English ships - many of whom carried coffee - by Napoleon I in the early nineteenth century, with the aim of ruining the British economy, have contributed to its now usual use, that of coffee substitute. As a mother of salads and chicory, chicory is widely consumed in Europe. It is known to contain a lot of provitamin A, vitamins B and C as well as mineral salts.",
                "Infusions, juices, decoctions, roasted roots",
                "Common",
                "Herbaceous",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h22 = new Item(
                "Citronella",
                "ic_citronella",
                "Perennial plant with its rhizomes (underground stems), lemongrass has in common with lemon only the smell it releases, when one crumples its leaves. Native to southern India and Sri Lanka, this plant of the family Poaceae (or grass) also grows in the tropical regions of the West Indies and Africa, where it is commonly used as a medicinal plant. Lemongrass is a herbaceous plant with long, narrow, linear, blue-green leaves with sharp edges. These leaves, with branch-shaped peduncles, measure from 90 cm to 2 m. The stem of lemongrass is hollow and bulbous: it is its base that is used in cooking to flavor dishes. Lemongrass has given its name to other plants with which it must not be confused: lemongrass verbena, lemongrass lemon balm and citronella aurone.",
                "Digestive and gastrointestinal disorders; fever, flu-like conditions; joint and muscle pain; rheumatism; states of fatigue, insomnia, stress, anxiety.",
                "0.60",
                "Recruit",
                "Poaceae",
                "If the medicinal properties of lemongrass are known since ancient Egypt, on the Indian subcontinent, this plant is used for even longer. In the West Indies, it is a traditional remedy for lowering fever, while in Africa it is used in the treatment of conditions such as tuberculosis and malaria. In tropical countries, it is often planted near homes, to keep insects away.",
                "Decoctions, infusions, essential oils",
                "Common",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h23 = new Item("Coriander", "ic_coriander", "Description", "Properties", "0.55", "Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h24 = new Item("Comfrey", "ic_comfrey", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h25 = new Item("Tarragon", "ic_tarragon", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h26 = new Item("Eucalyptus", "ic_eucalyptus", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h27 = new Item("Fennel", "ic_fennel", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h28 = new Item("Fenugreek", "ic_fenugreek", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h29 = new Item("Galanga", "ic_galanga", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h30 = new Item("Gentian", "ic_gentian", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h31 = new Item(
                "Ginger",
                "ic_ginger",
                "Ginger consists of two parts: the underground part, called the rhizome, is gnarled and branched and the aerial part is formed of leaves and a stem about one meter high. The rhizome, whose pulp is yellow inside, serves as a reserve for the plant and ensures its survival. The leaves are alternate, lanceolate and fragrant and the flowers are yellow with a red lip. The fruits contain few black seeds. Ginger multiplies and reproduces itself rather thanks to the division of its rhizome. It needs a wet, warm and sunny weather to grow, which is why it is usually found in tropical countries.",
                "Digestive disorders (nausea, vomiting, bloating, gas, intestinal pain), infection, colds, flu, cough, sore throat, fever, fatigue, lack of energy, muscle and joint pain, injury.",
                "0.84",
                "Recruit",
                "Zingiberaceae",
                "Ginger is grown in sunny and tropical areas, mainly in Asia (India, China, Nepal). There are written records of its use dating back more than 3,500 years, but it seems that its qualities have been known for more than 5,000 years. He is known in our regions thanks to the Mediterranean trade of Romans and Greeks. It is often used as a spice to decorate dishes. In Malaysia and India, where ginger comes from, it is a well-known medicinal plant, especially in Indian Ayurveda medicine which is very old. Today, it is used worldwide to treat intestinal disorders, flu-like conditions and depressed health.",
                "Capsules, ground ginger, herbal teas, fresh ginger, essential oils, tinctures",
                "Common",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h32 = new Item("Ginseng", "ic_ginseng", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h33 = new Item("Jasmine", "ic_jasmine", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h34 = new Item(
                "Lavender",
                "ic_lavender",
                "Lavender is a small dicotyledonous shrub of the family Labieae (or Lamiaceae), which measures 30 to 60 cm in height; its branches are fine and woody and there are narrow, pointed leaves at its base only. The flowers of lavender, a soft blue or purplish and shaped small corollas, are grouped in terminal spikes and give off a very pleasant fragrance. This plant grows only in rocky, but well drained, limestone and sunny. It is found throughout Mediterranean Europe, sometimes up to 1800 m altitude, especially in the Prealps of Provence. Excellent melliferous plant, lavender is very popular with bees.",
                "Dermatological problems (bactericidal, antiseptic); joint and rheumatic pains; irritation and / or respiratory inflammation; nervousness, anxiety, anxiety; insomnia: promotes sleep.",
                "1.07",
                "Recruit",
                "Labiatae",
                "Lavender is native to the West Mediterranean basin. The ancient Romans used it to perfume the baths and the linen. St. Hildegard of Bingen, in the twelfth century, gave it a place of choice in its natural pharmacopoeia and, at the same time, it was cultivated in the monasteries for its therapeutic properties. Since the Middle Ages, lavender was used in Provence in the manufacture of medicines and perfumes. From the nineteenth century, this aromatic plant has seen its culture grow in several European countries as well as in America. The region of Grasse, France, is now the \"capital\" of lavender, because of its large production of essential oil of this plant, which is used in herbal medicine but also in perfumery. It also grows in the following countries: Portugal, Spain, Balearic Islands, Somalia, India, Sahara and Australia.",
                "Essences, essential oils, decoctions, herbal teas, micronized powders, capsules, alcoholates, dyes, nebulisates",
                "Common",
                "Shrub",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h35 = new Item("Marjoram", "ic_marjoram", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h36 = new Item("Primrose", "ic_primrose", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h37 = new Item("Nettle", "ic_nettle", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h38 = new Item("Passiflora", "ic_passiflora", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h39 = new Item("Salad Burnet", "ic_salad_burnet", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h40 = new Item(
                "Rosemary",
                "ic_rosemary",
                "Rocky shrub in the wild, rosemary, family lamiaceae, can reach 2 m in height, in culture. It is easily recognized all year round, erected in the middle of the Mediterranean bushes: its evergreen leaves are rolled up on their edges. They are much longer than they are wide, of a dark green color, shiny on their upper surface and whitish on the underside. Its flowers, most often a purplish blue hue (whites are rarer) aggregate in short clusters, from February to May. Their calyx has a fluffy appearance, the corolla is bilabied and has four stamens, two of which extend beyond the upper lip. The fruit of rosemary, of globular form, is a brown tetrakene.",
                "The choline it contains acts as a regulator of lipids, in the liver, and promotes digestion.\n" +
                        "Its diuretic properties facilitate kidney activity and help prevent rheumatism.\n" +
                        "Its antioxidant properties have a stimulating effect on brain activity and improve memory.",
                "0.72",
                "Recruit",
                "Lamiaceae",
                "The use of rosemary in perfumery dates back to the 14th century. We then lend to the water of Hungary, the first alcoholic perfume inventoried and consisting essentially of rosemary, powers of water of youth. The legend claims, in fact, that rosemary allowed Elizabeth of Poland, cured of her paralysis and her problems of arthritis, to become queen of Hungary, seducing the king, despite her 72 years. However, it gave him stimulating effects on brain activity, from ancient Greece. It was for this reason that Greek intellectuals girded their heads with rosemary wreaths. Also used empirically, as a remedy for a good number of ailments, the honey of Narbonne, derived from rosemary, found a place of choice in the marine pharmacopoeia in the eighteenth century. Closer to home, in the 19th century, the German Catholic physician and priest Sebastian Kneipp, who actively contributed to the development of herbal medicine, prescribed rosemary baths for the elderly. They could thus fight against a number of ailments: general weakness, tiredness of the eyes, hypotonia, hypotension, hypercholesterolemia, cirrhosis, physical and intellectual overwork, with loss of memory, cardiac disorders of nervous origin, syncope, rheumatism or paralysis.",
                "Essential oils, capsules, ointments, tinctures, herbal teas, decoctions, inhalations, poultices, baths",
                "Common",
                "Shrub",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h41 = new Item("Rooibos", "ic_rooibos", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h42 = new Item("Rose", "ic_rose", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h43 = new Item("Saffron", "ic_saffron", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h44 = new Item("Sage", "ic_sage", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h45 = new Item("Elderberry", "ic_elderberry", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h46 = new Item(
                "Green Tea",
                "ic_green_tea",
                "Green tea, or virgin tea, is an evergreen shrub, native to the Far East. It can reach 30 m in height, but in cultivation it is generally maintained at a height of about 1.5 m. It produces fragrant white flowers. Depending on the variety, its leaves, dark green, can measure from 3 to 20 cm and have a glossy or matte appearance.",
                "Green tea, thanks to its high levels of caffeine and vitamin C, is a stimulant used to fight against fatigue. The consumption of tea promotes weight loss, especially in the case of anti-obesity treatments, and reduces cholesterol levels, thanks to the antioxidant action of polyphenols. Green tea, containing fluoride, helps prevent cavities.",
                "1.44",
                "Junior",
                "Theaceae",
                "Cultivated in Asia for millennia, green tea is one of the fundamental elements of traditional Asian medicine, in general, and Chinese in particular. Green tea has always been used for its stimulating properties, but also for its effective action against digestive infections. Caffeine, it contains, is traditionally used to fight against migraines. Finally, green tea has always been used in herbal medicine for its diuretic action. Traditional medicine advises against pregnancy, breastfeeding, insomnia and urinary problems, such as frequent urination.",
                "Infusions, herbal teas, capsules of total powder, extracts",
                "Common",
                "Shrub",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h47 = new Item("Linden", "ic_linden", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h48 = new Item("Red Clover", "ic_red_clover", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h49 = new Item(
                "Valerian",
                "ic_valerian",
                "Valerian is a perennial of the family Valerianaceae, up to 1 m in height. It has a vertical root with thick roots and opposite leaves, divided into pointed leaflets reminiscent of those of the fern. Its small, pale pink flowers show a high crescent inflorescence. Its essential oils give off a characteristic smell. Valerian enjoys humid climates and is cultivated in all temperate regions of the globe. It is grown in central and eastern Europe by sowing in spring, while its root and rhizome, used in herbal medicine, are harvested after two years in the fall.",
                "Anxiety, nervousness, stress, depression, sleep disorders, spasms and neuralgia, joint and muscle pain, high blood pressure due to stress.",
                "0.84",
                "Recruit",
                "Valerianaceae",
                "The name of valerian comes from the Latin valere which means \"to be well\". The characteristic odor of this plant, cultivated in Europe and North Asia, is at the origin of many legends and appellations. In the 1st century AD, in his De materia medica, reference work on more than six hundred medicinal plants, the Greek Dioscorides, doctor in the Roman army, pharmacologist and botanist, called phu, which means in Greek \"unpleasant smell\". It is this pronounced odor that is supposed to attract cats: hence the vernacular name of \"cat-weed\" given to this plant. Valerian has also been used since time immemorial by the Chinese and the Indians: the latter used its roots and used them to perfume their tobacco. In Greco-Roman antiquity, valerian is known for its soothing and relaxing virtues: Galen recognizes its sedative properties. In the Middle Ages, it is a universal remedy: hence its name \"cure-all\". It is used to fight against coughs, lack of breath, menstruation and vision disorders. It is also used to treat bruises and wounds, cuts and boils, as an antidote to poisons and as a cure for epilepsy. Nowadays, valerian is mainly used to soothe nervous disorders and to improve the quality of sleep. It is also an effective pain reliever for headaches and a relaxant used in case of muscle or joint pain.",
                "Powders, tablets, capsules, decoctions, tinctures.",
                "Common",
                "Perennial",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h50 = new Item("Vanilla", "ic_vanilla", "Description", "Properties", "0.55","Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h51 = new Item("Verbena", "ic_verbena", "Description", "Properties", "0.55", "Recruit", "Race", "History", "Combination", "Common", "Aromatic", 0, "", "", "", 0, "herb");
        Item h52 = new Item(
                "Mint",
                "ic_mint",
                "Mint is a very aromatic plant up to 80 cm tall. It belongs to the lamiaceae family. Its most used variety in herbal medicine is peppermint. It has a serrated structure with square rods. Its color is green and its harvest is carried out annually. It sows in the spring and is harvested in summer. It is found in Europe, Asia and North America.",
                "Digestive disorders (difficult digestion, bloating, flatulence), inflammation of the respiratory system, joint pain, muscle pain or headache. Itchy skin or oral infection.",
                "0.72",
                "Recruit",
                "Lamiaceae",
                "Mint is one of the best known medicinal plants. Archaeologists have found mint leaves in Egyptian tombs. Its use is proven in the Greeks and Romans to relieve pain or purge patients. Fell into oblivion in the West, it only joined the traditional pharmacopoeia in the eighteenth century. Since then, it has been one of the first plants to be used extensively by the pharmaceutical industry. Menthol has become one of the classics of pharmacy stalls. We also find mint in a large number of candies, syrups or as a flavor intended to improve the taste of certain drugs.",
                "Drying, tablets, capsules, sweets, oils, ointments, lotions, infusions, tinctures, decoctions",
                "Common",
                "Aromatic",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h53 = new Item(
                "Lemon",
                "ic_lemon",
                "Lemon (or lemon) is a citrus, fruit of the lemon tree, whose juice has a pH of 2.5. The lemon tree (Citrus limon) is a shrub 5 to 10 meters high, evergreen, family Rutaceae.\n" +
                        "\n" +
                        "This ripe fruit has a bark that ranges from tender green to bright yellow under the action of the cold. Maturity is late autumn and early winter in the northern hemisphere. Its flesh is juicy, acidic and rich in vitamin C, which is worth it - with its easy conservation - to have been spread all over the planet by browsers who use it to prevent scurvy. From the bark is extracted an essential oil which contains among others limonene and citral substances.\n" +
                        "\n" +
                        "Sweet lemons are fruits of juice cultivars with little or no acid nevertheless classified Citrus limon (L.) Burm. f. (Tanaka classification).",
                "Advertisements regarding the benefits of lemon may contain false, ambiguous or misleading health claims. Thus, lemon is lent all kinds of virtues: high in vitamin C, it would erase all fatigue, promote digestion, make white teeth, would be diuretic. In addition to its anti-infective, antiseptic and healing properties, it reduces cellulite (fat-burning food), slows down aging and even helps protect against cancer.\n" +
                        "\n" +
                        "Most of these claims are not scientifically proven. However, its richness in vitamin C promotes healing. The citric acid of the juice is antiseptic, hence the gargles with juice cut water in case of sore throat and the addition of a few drops of lemon juice in seafood eaten raw. The lemon rubbed on the teeth, instead of whitening them (eliminating the stains of tea, tobacco), attacks the enamel on which the repeated acidity can lead to demineralization of the teeth and create micropores sufficient to let the bacteria enter. origin of caries. Finally, this fruit remains interesting for health because of its richness in vitamin C, calcium, phosphorus and potassium whose assimilation is favored by citric acid.\n" +
                        "\n" +
                        "Lemon can even have significant adverse effects. Lemon, like all citrus fruit, is rich in furanocoumarines (psoralen, bergaptene, especially present in the pulp, these components are therefore more concentrated in fresh fruit juice than in pasteurized fruit juice) which can lead to photosensitization. High citrus consumption increases the risk of melanoma by 30%. Thus, the frequent consumption of fresh fruit juice would slow the progression of certain cancers but would favor that of the skin for people who do not protect themselves from the sun.",
                "0.99",
                "Recruit",
                "Race",
                "Phanias d'Eresa seems to make us suspect that lemon can derive its name from the word cedron.\n" +
                        "\n" +
                        "The origin of the yellow lemon has long remained unknown, notably because of its polymorphism and inter-varietal diversity. The researchers located his wild ancestor in the region of Assam, the Indo-Burmese region or China. Phylogenetic studies in 2016 show that he was born in the Mediterranean and comes from a hybrid between bigarade (or bitter orange) and citron around the 5th millennium BC. AD\n" +
                        "\n" +
                        "The lemon tree was originally used as an ornamental plant in recreational gardens in the Middle Ages, including Islamic gardens. The lemon is gradually introduced into the medieval diet where it is used as an acid fund intended essentially for the development of raw vegetables or seasoning any food in the same way as verjuice, vinegar or orange juice. However, it is likely that it has been used as a technique for preserving meat with acid since ancient times.",
                "Cakes, pies, juices, zests, infusions, essential oils.",
                "Common",
                "Fruit",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h54 = new Item(
                "Cayenne Pepper",
                "ic_cayenne_pepper",
                "There are two main varieties of the plant, one American and the other African. The African variety is the most pungent, with its small yellow red pods. And it is with capsaicin, active ingredient of Cayenne Pepper that we owe the majority of the virtues of the plant.",
                "The plant has a nutritional nutritional value, but a medicinal value too. Its ancestral medical uses range from healing ulcers (stimulating gastrointestinal secretions), congestion and colds, to neuralgia, low back pain, high blood pressure, indigestion, and even kidney problems. In addition to this, cayenne pepper is a stimulant and activator. That is, it improves the metabolism, and makes the benefits of other substances, including herbs, effective for the body.\n" +
                        "\n" +
                        "The plant is also anti-fungal. Some studies show efficacy against diabetes.\n" +
                        "\n" +
                        "The contraindications are not numerous, mainly allergy to this plant, hemorrhoids, hypertension, and gastritis.",
                "0.30",
                "Recruit",
                "Race",
                "The Aztecs gave the Cayenne the name of Chili. The natives of Equatorial America, where the plant comes from, know the culinary and medicinal virtues of its fruits since at least 9000 years. Archaeologists believe that it was already cultivated in Mexico about 7,000 years ago.\n" +
                        "\n" +
                        "It was Dr. Diego Alvarez Chanca, a companion of Christopher Columbus, who introduced the plant to the Europeans, following a famous voyage of exploration that ended in India, but ended in the Caribbean. From the European continent, fruits migrated rapidly to all equatorial regions of the globe and were adopted by many cultures in Asia, the Middle East and Africa, as well as in southern Europe, especially in southern Italy. In fact, few spices are known to be so universally widespread.\n" +
                        "\n" +
                        "The Cayenne pepper enjoys an enviable reputation in all its cultures for its digestive and therapeutic virtues, and the humans who live in the tropics have quickly understood that it allows the body to better withstand the intense heat that occurs in these latitudes.\n" +
                        "\n" +
                        "Among the therapeutic uses attributed to it in these different traditions, modern medicine has mainly retained the analgesic effects of capsaicin, the substance which gives the Cayenne pepper its characteristic piquant flavor. However, it seems that the virtues of cayenne are more varied and numerous than the topical uses currently recognized in the West.",
                "Exotic cuisine, sauce, marinade, infusions",
                "Common",
                "Chilli Pepper",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h55 = new Item(
                "Lime",
                "ic_lime",
                "The lime tree is a wild and ornamental tree about 15 to 30 m tall, deciduous, whose flowers are light yellow. Tree very present in Europe and Asia Minor, it grows wild in various spaces: forests, fields, but also in the gardens. The trunk is rather short and its summit is very ramified. Its summer bloom allows the harvest of its flowers, grouped in the form of bunches and particularly fragrant. The leaves, deciduous, are light green, toothed and heart-shaped. The inside of the leaves is covered with a fine down, along the veins. The fruits of the lime tree are in the form of small capsules. The lime trees can live up to four hundred years.\n",
                "The lime tree is known for its tranquilizing effects. It reduces stress and improves anxiety states and sleep disorders. It also helps to lower blood pressure and has diuretic effects. Other usual uses are the relief of digestive disorders such as spasms and the symptoms of colds.",
                "0.95",
                "Recruit",
                "Tiliaceae",
                "Throughout the ages, we find evidence of preparations made from flowers or peel cooked, crushed and mixed with culinary preparations. Linden has always been used for its sedative and soothing properties. The best-known form is the herbal tea, made from dried flowers and leaves, which has been used since the 16th century. As early as 1709, the king 's doctor wrote that \"a decoction of linden wood, especially young branches, relieves hydropics\" (patients suffering from edema or heart failure). During the Second World War, dried linden leaves were sifted into flour, appreciated for its nutritional qualities and its protein intake. With 1 kg of fresh leaves, 300 g of flour were obtained. Linden is now one of the most popular medicinal plants in France. Since the nineteenth century, there is in France an industrial production of linden flowers, for herbal medicine and marketed in various forms.\n",
                "Infusions, decoctions, mother tinctures, aqueous extracts, homeopathic dilutions, honey, capsules",
                "Common",
                "Tree",
                0,
                "",
                "",
                "",
                0,
                "herb");
        Item h56 = new Item(
                "Lemon Verbena",
                "ic_lemon_verbena",
                "The fragrant verbena is a small shrub with a main stem woody, 1 to 3 m high, deciduous.\n" +
                        "\n" +
                        "Leaves lanceolate, pointed, almost sessile (petiole very short), pale green and arranged by 3. They exhale a strong smell of lemon when crumpled.\n" +
                        "\n" +
                        "The small flowers, white or pale mauve, are grouped in loose spikes about 10 cm long. They do not grow in Europe.\n" +
                        "\n" +
                        "The main components in citronella verbena oil are citral (30-35%), nerol and geraniol.",
                "Nervous fatigue: anxiety, anxiety, stress, mild depression and related gastrointestinal disorders. Difficult digestions and gastric disturbances.",
                "0.96",
                "Recruit",
                "Verbenaceae",
                "The first European botanist who noticed lemon verbena was Frenchman Philibert Commerson, who in 1767 took a specimen from Buenos Aires during his botanical circumnavigation trip with Bougainville. However, the plant had previously been discreetly imported directly to Real Botanical Garden of Madrid, where in 1797 the teachers Casimiro Gómez de Ortega and Antonio Palau y Verdera, without devoting him an official publication, give him the name Aloysia citrodora in Latin and Hierba Princesa (Herb of the Princess) in Spanish, in honor of Marie-Louise of Parma, Princess of Asturias, the wife of Infant Charles de Bourbon, owner of the garden and son of King Charles III. This name is actually published in the first volume of Parte Práctica de Botánica de Palau in 1784.\n" +
                        "\n" +
                        "Lemon verbena is also brought back from Peru by the French naturalist Joseph Dombey (1742-1795), who manages to introduce it in Europe with difficulty. Dombey did not even obtain permission from the Spanish authorities to plant seeds and when his collections of American plants were landed in Cadiz in 1785, they were seized and left to rot in the warehouses. He manages to survive a lemongrass verbena plant, with some other plants harvested during eight years in Peru. The botanist Deleuze, will report later the discovery in these terms:\n" +
                        "\n" +
                        "\"Of all the plants that Dombey has made known to us, the most interesting is the verbena with lemon scent (verbena triphylla L'Her.). This shrub, which rises to 15 feet, is of all the plants which can be cultivated in Europe, the one whose foliage has the most delicious perfume. In Paris one is obliged to shelter it in the orangery during the strong frosts; in more temperate climates, it spends the winter in the ground. Already we see hedges at Florence, and M. de Ruffo has cultivated it successfully in the department of Basses-Alpes. When it is more widespread in the south of France, it will line the roads and form small groves that, by the elegance of the shrubs, the lightness of their panicles of flowers of a gray of linen, the green cheerful their foliage, and especially their sweet and vivifying perfume, will appear much preferable to the myrtle groves so much celebrated by the poets. The dried leaves retain all their odor, and the infusion is very pleasant and salutary. If, as some authors have thought, this shrub must be separated from verbenas, and make a genus apart, it is to him that we would like to be called Dombey. \"\n" +
                        "\n" +
                        "At the same time Gómez Ortega sends seeds and specimens of the plant to Charles Louis L'Héritier de Brutelle in Paris, who publishes it under the name Verbena triphylla in the second issue of his stirpes Novae in December 1785 or January 1786 From Paris, John Sibthorpe, a professor of botany at Oxford, gets a specimen he introduces into British horticultural circles: in 1797 lemongrass verbena is spread in greenhouses around London and its popularity as a essence of scented bouquets grows during the next century. They win the Royal Horticultural Society's Award of Garden Merit.",
                "Infusions, essential oil",
                "Common",
                "Shrub",
                0,
                "",
                "",
                "",
                0,
                "herb");

        Item r1 = new Item(
                "Diurectic Tea",
                "ic_diuretic_tea",
                "Pour a cup of boiling water over the green tea and herbs, then let infuse 3 minutes. Filter in the cup. Sugar with honey if you want, stir and sip.",
                "Green tea is beneficial for chronic coughs, colds and sore throats. It stimulates cellular regeneration.",
                "10.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "1 Green Tea, 3 Mint, 1 Lemon Verbena, 1 Lemon, 1 Honey, 1 Sugar",
                "Easy",
                "Weak, Sore throat, Cough",
                1,
                "recipe");
        Item r2 = new Item(
                "Soothing Tea",
                "ic_soothing_tea",
                "Pour a cup of boiling water over the ginger before crushing it with the back of a spoon. Add the remaining ingredients and stir. Let infuse 5 minutes. Filter into the cup and sip.",
                "This herbal tea is excellent in case of colds. Cayenne pepper is a fabulous antiseptic for the throat. Sip 3 cups of herbal tea a day for 2 days.",
                "15.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                0,
                "6 Ginger, 2 Lemon, 2 Honey, 1 Cayenne Pepper",
                "Easy",
                "Cough, Headache, Runny nose",
                1,
                "recipe");
        Item r3 = new Item(
                "Relaxing Tea",
                "ic_relaxing_tea",
                "This relaxing infusion has a relaxing and anti-stress effect. It is effective against muscle tension and spasms",
                "Pour a cup of boiling water over linden, lavender, ginger and cardamom. Let infuse 5 minutes. Filter into the cup, add the honey and stir. Drink slowly.",
                "7.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                0,
                "1 Lime, 2 Ginger, 1 Lavender, 1 Cardamom, 1 Honey",
                "Easy",
                "Tired, Nervous, Tensed, Stressed, Muscle Pain",
                1,
                "recipe");
        Item r4 = new Item(
                "Rejuvenating Infusion",
                "ic_rejuvenating_infusion",
                "Pour two cups of boiling water over the rosemary. Let infuse 7 minutes, then add the honey and stir. Filter. Sip a half cup of this tonic, hot or cold, 4 times a day.",
                "Pure rosemary toner has a beneficial effect on the physical appearance",
                "3.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                0,
                "1 Rosemary, 1 Honey",
                "Easy",
                "None",
                1,
                "recipe");
        Item r5 = new Item(
                "Calming Infusion",
                "ic_calming_infusion",
                "Versez deux tasses d'eau bouillante sur les ingrédients et laissez infuser 8 minutes. Filtrez puis remuez. Buvez à petites gorgées une demi-tasse de ce tonifiant, chaud ou froid, 4 fois par jour.",
                "Thanks to its relaxing effects, this toner helps you to have a good night's sleep.",
                "3.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                0,
                "1 Lavender, 1 Valerian, 1 Honey",
                "Easy",
                "None",
                1,
                "recipe");

        Item o1 = new Item(
                "Water",
                "ic_water",
                "Water is a chemical substance made up of H2O molecules. This compound is very stable and nevertheless very reactive. In many contexts the term water is used in the restricted sense of water in the liquid state, and it is also used to designate a dilute aqueous solution (fresh water, drinking water, sea water, lime water, etc.). . Liquid water is an excellent solvent.\n" +
                        "\n" +
                        "Water is ubiquitous on Earth and in the atmosphere, in its three states, solid (ice), liquid and gaseous (water vapor). Extraterrestrial water is also abundant, in the form of water vapor in space and in condensed form (solid or liquid) on the surface, near the surface or inside a large number of celestial objects. .\n" +
                        "\n" +
                        "Water is an important biological constituent, and liquid water is essential for all known living organisms. Given its vital nature, its importance in the economy and its unequal distribution on Earth, water is a natural resource whose management is subject to strong geopolitical issues.",
                "The human body needs about 2.5 liters of water per day (1.5 liters in liquid form and 1 liter in absorbed food), more in case of physical exercise or heat; it is not necessary to wait until you are thirsty to absorb it, especially for pregnant women and for the elderly in whom the feeling of thirst is delayed. Without water, death occurs after 2 to 5 days, without any effort (40 days without food at rest).",
                "0.10",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "",
                "",
                "",
                1,
                "other");
        Item o2 = new Item(
                "Milk",
                "ic_milk",
                "Milk is a generally whitish, edible biological fluid produced by the mammary glands of female mammals. Rich in lactose, it is the main source of nutrients for young mammals before they can digest other types of foods. Milk in early lactation contains colostrum, which carries the mother's antibodies to reduce the risk of many diseases in the newborn. It also contains many other nutrients.\n" +
                        "\n" +
                        "The man uses the milk produced by some domestic mammals, mainly that of the cow, as a food. Around the world, dairy farms produced around 730 million tonnes of milk in 2011, consumed in India, Europe, Australia, the United States, China and Russia.",
                "The milk contains calcium which is present in a form allowing an intestinal absorption of the order of 30%. Calcium helps to ensure bone strength and protect against osteoporosis provided you do not run out of vitamin D that absorbs the calcium ingested. Calcium is also present in many common foods such as cabbage or dried fruits. For a balanced diet, dairy products are not essential. The recommended daily intakes (RDA) of calcium are 800 mg in the European Union. However, according to the nutrition department of the Harvard School of Public Health, the right amount of calcium in our diet has yet to be determined. The WHO notes that 500 mg values \u200B\u200Bdo not always lead to deficiency, especially in developing countries.\n" +
                        "\n" +
                        "Milk provides protein, vitamins and trace elements, including zinc and selenium, and omega-3s.\n" +
                        "\n" +
                        "In 2011, a meta-analysis found no association between milk consumption and protection against hip fractures in adults and the elderly.",
                "0.30",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "",
                "",
                "",
                1,
                "other");
        Item o3 = new Item(
                "Rhum",
                "ic_rhum",
                "Rum (English: rum, Spanish: ron) is a brandy originating in the Americas, processed from sugar cane or by-products of the sugar industry. It is consumed white, aged in barrel (old rum) or spicy. It then takes an amber color more or less dark. Depending on the products used during its preparation, it can be called agricultural or industrial.",
                "Rum, like any other alcohol, gives us a feeling of warmth and well-being. In addition, it would help fight infection and reduce inflammation through its energy intake. However, its action is decried by some because its effects do not last long, and the alcohol does not have that good sides: it will end up cooling and tired the body.",
                "1.00",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "Protocol",
                "",
                "",
                1,
                "other");
        Item o4 = new Item(
                "Sugar",
                "ic_sugar",
                "Sugar is a sweet-flavored substance extracted mainly from sugar cane and sugar beet. Sugar is a molecule of sucrose (glucose + fructose).\n" +
                        "\n" +
                        "The term \"sugar\" comes from the Italian term \"zucchero\", itself borrowed from the Arabic \"sukkar\" (سكر), a word of Indian origin, in Sanskrit \"çârkara\".\n" +
                        "\n" +
                        "It is also possible to obtain sugar from other plants.\n" +
                        "\n" +
                        "However, other compounds from the same family of saccharides also have a mild flavor: glucose, fructose ... which are increasingly used by the agri-food industry and in other sectors. On a nutrition label, the term \"sugars\" (with an S) refers to all \"sweet\" carbohydrates with a sweetening power, mainly fructose, sucrose, glucose, maltose and lactose. The other carbohydrates with a sweetening power are \"polyols\" (sorbitol, maltitol, mannitol) but they are now labeled separately, as \"polyalcohols\", which are carbohydrates but not sugars.",
                "Glucose: It is a sugar consisting of a single molecule. It is in the form of a white powder. Glucose has a low sweetening power, from 70 to 75. It is therefore much less used in the food industry. Instead, it is used as a bulking agent (= food product that weighs a food or a preparation without changing its energy value) (in the form of concentrated syrup, for example). Glucose is ubiquitous in our blood. It comes mainly from ingested carbohydrates or sugar in food.\n" +
                        "\n" +
                        "Fructose: It is an ose (simple non-hydrolysable sugar). It is used as a sweetener in biscuits and industrial pastries. By its high sweetening power (130 to 150), fructose allows, for a given sweet level, to reduce the total amount of carbohydrates and reduce energy intake. But its excessive consumption is associated with an increased risk of cardiovascular disease.",
                "0.10",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "",
                "",
                "",
                1,
                "other");
        Item o5 = new Item(
                "Honey",
                "ic_honey",
                "Honey is a sweet substance made by honey bees from nectar or honeydew. They store it in the hive and feed on it throughout the year, especially during bad weather. It is also consumed by other animal species, including the human species that organizes its production by raising honey bees.",
                "The consumption of honey and brood (bee larvae naturally contained in unfiltered honey) may have been useful to the evolution of man, especially the development of his brain, honey containing a significant amount of protein and of fats. Rock paintings show that the first men \"hunted\" bee hives. Today, the Hadza people have kept this tradition and enjoy mutualism with a local wild bird to help them find hives. The Hadzas thus spend, during the rainy season, 4 to 5 hours a day to look for honey.",
                "0.95",
                "Recruit",
                "",
                "",
                "",
                "",
                "",
                1,
                "",
                "",
                "",
                1,
                "other");

        //db.itemDAO().insertItem(h1);
        //db.itemDAO().insertItem(h2);
        //db.itemDAO().insertItem(h3);
        //db.itemDAO().insertItem(h4);
        //db.itemDAO().insertItem(h5);
        //db.itemDAO().insertItem(h6);
        //db.itemDAO().insertItem(h7);
        db.itemDAO().insertItem(h8);
        //db.itemDAO().insertItem(h9);
        //db.itemDAO().insertItem(h10);
        //db.itemDAO().insertItem(h11);
        //db.itemDAO().insertItem(h12);
        //db.itemDAO().insertItem(h13);
        //db.itemDAO().insertItem(h14);
        //db.itemDAO().insertItem(h15);
        //db.itemDAO().insertItem(h16);
        //db.itemDAO().insertItem(h17);
        //db.itemDAO().insertItem(h18);
        //db.itemDAO().insertItem(h19);
        db.itemDAO().insertItem(h20);
        //db.itemDAO().insertItem(h21);
        db.itemDAO().insertItem(h22);
        //db.itemDAO().insertItem(h23);
        //db.itemDAO().insertItem(h24);
        //db.itemDAO().insertItem(h25);
        //db.itemDAO().insertItem(h26);
        //db.itemDAO().insertItem(h27);
        //db.itemDAO().insertItem(h28);
        //db.itemDAO().insertItem(h29);
        //db.itemDAO().insertItem(h30);
        db.itemDAO().insertItem(h31);
        //db.itemDAO().insertItem(h32);
        //db.itemDAO().insertItem(h33);
        db.itemDAO().insertItem(h34);
        //db.itemDAO().insertItem(h35);
        //db.itemDAO().insertItem(h36);
        //db.itemDAO().insertItem(h37);
        //db.itemDAO().insertItem(h38);
        //db.itemDAO().insertItem(h39);
        db.itemDAO().insertItem(h40);
        //db.itemDAO().insertItem(h41);
        //db.itemDAO().insertItem(h42);
        //db.itemDAO().insertItem(h43);
        //db.itemDAO().insertItem(h44);
        //db.itemDAO().insertItem(h45);
        db.itemDAO().insertItem(h46);
        //db.itemDAO().insertItem(h47);
        //db.itemDAO().insertItem(h48);
        db.itemDAO().insertItem(h49);
        //db.itemDAO().insertItem(h50);
        //db.itemDAO().insertItem(h51);
        db.itemDAO().insertItem(h52);
        db.itemDAO().insertItem(h53);
        db.itemDAO().insertItem(h54);
        db.itemDAO().insertItem(h55);
        db.itemDAO().insertItem(h56);

        db.itemDAO().insertItem(r1);
        db.itemDAO().insertItem(r2);
        db.itemDAO().insertItem(r3);
        db.itemDAO().insertItem(r4);
        db.itemDAO().insertItem(r5);

        db.itemDAO().insertItem(o1);
        db.itemDAO().insertItem(o2);
        db.itemDAO().insertItem(o3);
        db.itemDAO().insertItem(o4);
        db.itemDAO().insertItem(o5);

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
