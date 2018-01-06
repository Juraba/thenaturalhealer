package com.marmot.intrepid.naturalhealer.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.marmot.intrepid.naturalhealer.model.enumerations.HerbRarity;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbType;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;
import com.marmot.intrepid.naturalhealer.model.enumerations.RecipeDifficulty;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;

import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class Villager {
    @PrimaryKey
    @NonNull
    private String name;
    @ColumnInfo(name = "pic_name")
    private String picName;
    @Ignore
    private ArrayList<Quest> quests;

    public Villager(String name, String picName){
        quests = new ArrayList<Quest>();
        this.name = name;
        this.picName = picName;
    }

    public String getName() {return this.name;}
    public void setName(String s) {this.name = s;}

    public String getPicName() {return this.picName;}
    public void setPicName(String s) {this.picName = s;}

    public ArrayList<Quest> getQuests() {return this.quests;}

    public void addQuest(Quest quest){this.quests.add(quest);}

    /**
    //Méthode temporaire
    public static ArrayList<Villager> loadVillagers() {

        ArrayList<Villager> villagers = new ArrayList<Villager>();

        //CREATION DES VILLAGEOIS
        Villager v1 = new Villager("Mme.SEGUIN", "seguin.png");
        Villager v2 = new Villager("M.LE MAIRE", "lemaire.png");
        Villager v3 = new Villager("Mme.ROSSIGNOL", "rossignol.png");
        Villager v4 = new Villager("M.BROSSARD", "brossard.png");
        Villager v5 = new Villager("M.RABAULT", "rabault.png");

        //CREATION DE QUETES
        //String description, HashMap<Item, Integer> requirements, int rewardMoney, int rewardXp, boolean cancelable, String type
        HashMap<Item, Integer> intQ1 = new HashMap<>();
        Symptoms[] s1 = {Symptoms.COUGH, Symptoms.HEADACHE, Symptoms.RUNNYNOSE};
        intQ1.put(new Herb("Thym", "ic_herb_thyme", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON), 10);
        intQ1.put(new Recipe("Onguent", "ic_recipe_ointment",  "Description", "Properties", 10.00, new Rank(RankEnum.RECRUIT), RecipeDifficulty.EASY, s1, "Protocol"), 10);
        intQ1.put(new OtherIngredients("Water", "ic_other_water", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT)), 10);

        Quest q1 = new Quest("Douleurs musculaires", "Madame SEGUIN a des douleurs musculaires dues à sa vieillesse, trouvez de quoi la soulager !", intQ1, 500, 10, true, QuestType.MAIN);
        Quest q2 = new Quest("Stress intense", "Monsieur LE MAIRE est très stressé à cause de son travail, aidez-le à retrouver sa sérénité", intQ1, 500, 10, true, QuestType.DAILY);
        Quest q3 = new Quest("Sommeil fuyard", "Monsieur LE MAIRE a du mal à trouver le sommeil ces derniers temps, auriez-vous de quoi l'apaiser ?", intQ1,500,10,true, QuestType.MAIN);
        Quest q4 = new Quest("Mal de tête", "Madame ROSSIGNOL subit un affreux mal de tête au travail depuis quelques temps, essayez de stopper cet enfer !", intQ1, 500, 10, true, QuestType.MAIN);
        Quest q5 = new Quest("Problème de poids", "Monsieur BROSSARD a pris quelques kilos après les fêtes, concotez de quoi stimuler sa perte de poids !", intQ1, 500, 10, true, QuestType.EVENT);
        Quest q6 = new Quest("Rage de dents", "Monsieur RABAULT a une rage de dents depuis maintenant deux jours, aidez-le à supprimer sa douleur", intQ1, 500, 10, true, QuestType.MAIN);

        //AJOUT DES LISTES AUX VILLAGEOIS
        v1.addQuest(q1);
        v2.addQuest(q2);
        v2.addQuest(q3);
        v3.addQuest(q4);
        v4.addQuest(q5);
        v5.addQuest(q6);

        //AJOUT DES VILLAGEOIS A LA LISTE
        villagers.add(v1);
        villagers.add(v2);
        villagers.add(v3);
        villagers.add(v4);
        villagers.add(v5);

        return villagers;
    }*/
}
