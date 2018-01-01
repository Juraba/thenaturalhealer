package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.ItemCategories;

import java.util.ArrayList;

public abstract class AbstractItemHandler {

    private ArrayList<Herb> herbs;
    private ArrayList<Recipe> recipes;
    private ArrayList<OtherIngredients> otherIngredients;

    public AbstractItemHandler(){
        this.herbs = new ArrayList<Herb>();
        this.recipes = new ArrayList<Recipe>();
        this.otherIngredients = new ArrayList<OtherIngredients>();
    }

    public ArrayList<Herb> getHerbs() {return this.herbs;}

    public ArrayList<Recipe> getRecipes() {return this.recipes;}

    public ArrayList<OtherIngredients> getOtherIngredients() {return this.otherIngredients;}

    public void switchCategory(ItemCategories category) {
        switch(category) {
            case HERBS:
                display(this.herbs);
                break;
            case RECIPES:
                display(this.recipes);
                break;
            case OTHER:
                display(this.otherIngredients);
                break;
            default:
                display(this.herbs);
        }
    }

    public void display(ArrayList array) {

    }

    //Méthodes temporaires
    public static Grimoire loadGrimoire(ArrayList<Item> items) {

        Grimoire grimoire = new Grimoire();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getClass() == Herb.class) {
                grimoire.getHerbs().add((Herb) items.get(i));
            }
            else if (items.get(i).getClass() == Recipe.class) {
                grimoire.getRecipes().add((Recipe) items.get(i));
            }
            else if (items.get(i).getClass() == OtherIngredients.class) {
                grimoire.getOtherIngredients().add((OtherIngredients) items.get(i));
            }
        }

        return grimoire;
    }

    public static Shop loadShop(ArrayList<Item> items) {

        Shop shop = new Shop();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getClass() == Herb.class) {
                shop.getHerbs().add((Herb) items.get(i));
            }
            else if (items.get(i).getClass() == Recipe.class) {
                shop.getRecipes().add((Recipe) items.get(i));
            }
            else if (items.get(i).getClass() == OtherIngredients.class) {
                shop.getOtherIngredients().add((OtherIngredients) items.get(i));
            }
        }

        return shop;
    }
}
