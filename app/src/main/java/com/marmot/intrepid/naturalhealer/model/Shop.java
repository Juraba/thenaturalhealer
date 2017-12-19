package com.marmot.intrepid.naturalhealer.model;

import com.marmot.intrepid.naturalhealer.model.enumerations.ItemCategories;
import java.util.ArrayList;

/**
 * Created by Camille K on 19/12/2017.
 */

public class Shop {

    private ArrayList<Herb> herbs;
    private ArrayList<Recipe> recipes;

    public Shop(){
        this.herbs = new ArrayList<Herb>();
        this.recipes = new ArrayList<Recipe>();
    }

    public void switchCategory(ItemCategories category) {
        switch(category) {
            case HERBS:
                display(herbs);
                break;
            case RECIPES:
                display(recipes);
                break;
            default:
                display(herbs);
        }
    }

    public void display(ArrayList array) {

    }

}
