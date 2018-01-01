package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.HerbRarity;
import com.marmot.intrepid.naturalhealer.model.enumerations.HerbType;
import com.marmot.intrepid.naturalhealer.model.enumerations.RankEnum;
import com.marmot.intrepid.naturalhealer.model.enumerations.RecipeDifficulty;
import com.marmot.intrepid.naturalhealer.model.enumerations.Symptoms;

import java.util.ArrayList;

public class Item {
    private String name, description, properties;
    private double price;
    private Rank rank;

    public Item(String name, String description, String properties, double price, Rank rank){
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.price = price;
        this.rank = rank;
    }

    public String getName() {return this.name;}

    public String getDescription() {return this.description;}

    public String getProperties() {return this.properties;}

    public double getPrice() {return this.price;}

    public Rank getRank() {return this.rank;}

    //Méthode temporaire
    public static ArrayList<Item> loadItems() {

        ArrayList<Item> items = new ArrayList<Item>();

        //CREATION DES PLANTES
        Herb h1 = new Herb("Thym", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON);
        Herb h2 = new Herb("Basilic", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON);
        Herb h3 = new Herb("Millepertuis", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON);
        Herb h4 = new Herb("Menthe", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON);
        Herb h5 = new Herb("Camomille", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT), "Race", HerbRarity.COMMON, "History", "Combination", HerbType.COMMON);

        //CREATION DES RECETTES
        Symptoms[] s1 = {Symptoms.COUGH, Symptoms.HEADACHE, Symptoms.RUNNYNOSE};
        Symptoms[] s2 = {Symptoms.NERVOUS, Symptoms.MUSCLEPAIN, Symptoms.STRESSED, Symptoms.TENSED, Symptoms.TIRED};
        Recipe r1 = new Recipe("Tisane", "Description", "Properties", 10.00, new Rank(RankEnum.RECRUIT), RecipeDifficulty.EASY, s2, "Protocol");
        Recipe r2 = new Recipe("Onguent", "Description", "Properties", 10.00, new Rank(RankEnum.RECRUIT), RecipeDifficulty.EASY, s1, "Protocol");
        Recipe r3 = new Recipe("Soupe", "Description", "Properties", 10.00, new Rank(RankEnum.RECRUIT), RecipeDifficulty.EASY, s1, "Protocol");
        Recipe r4 = new Recipe("Gâteau", "Description", "Properties", 10.00, new Rank(RankEnum.RECRUIT), RecipeDifficulty.EASY, s1, "Protocol");

        //CREATION DES AUTRES INGREDIENTS
        OtherIngredients o1 = new OtherIngredients("Water", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT));
        OtherIngredients o2 = new OtherIngredients("Milk", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT));
        OtherIngredients o3 = new OtherIngredients("Rhum", "Description", "Properties", 0.55, new Rank(RankEnum.RECRUIT));

        //AJOUT A LA LISTE DES ITEMS
        items.add(h1);
        items.add(h2);
        items.add(h3);
        items.add(h4);
        items.add(h5);
        items.add(r1);
        items.add(r2);
        items.add(r3);
        items.add(r4);
        items.add(o1);
        items.add(o2);
        items.add(o3);

        return items;
    }
}

