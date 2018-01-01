package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum HerbType {
    COMMON("Aromatic", "Aromatiques"),
    RARE("Rare", "Sauvages"),
    UNIQUE("Unique", "Unique"),
    LEGENDARY("Legendary", "Légendaire");

    private String en = "";
    private String fr = "";

    HerbType(String en, String fr){
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
