package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum HerbRarity {
    COMMON("Common", "Commun"),
    RARE("Rare", "Rare"),
    UNIQUE("Unique", "Unique"),
    LEGENDARY("Legendary", "Légendaire");

    private String en = "";
    private String fr = "";

    HerbRarity(String en, String fr){
        this.en = en;
        this.fr = fr;
    }

    public String getEn() {
        return this.en;
    }

    public String getFr() {
        return this.fr;
    }

    public static HerbRarity findEn(String rarity){
        HerbRarity ret = null;
        for(HerbRarity r : values()){
            if( r.getEn().equals(rarity)){
                ret = r;
            }
        }
        return ret;
    }

    public static HerbRarity findFr(String rarity){
        HerbRarity ret = null;
        for(HerbRarity r : values()){
            if( r.getFr().equals(rarity)){
                ret = r;
            }
        }
        return ret;
    }
}
