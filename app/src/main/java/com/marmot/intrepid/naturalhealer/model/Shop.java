package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.ItemCategories;

import java.util.ArrayList;

public class Shop {

    private ArrayList<Herb> herbs;
    private ArrayList<Recipe> recipes;
    private ArrayList<OtherIngredients> otherIngredients;

    public Shop(){
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

}
