package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum ItemCategories {
    HERBS("Herbs", "Plantes"),
    RECIPES("Recipies", "Recettes");

    private String en = "";
    private String fr = "";

    ItemCategories(String en, String fr){
        this.en = en;
        this.fr = fr;
    }

    public String getEn() {
        return this.en;
    }

    public String getFr() {
        return this.fr;
    }
}
