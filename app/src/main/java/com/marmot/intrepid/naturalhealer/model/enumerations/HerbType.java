package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum HerbType {
    AROMATIC("Aromatic", "Aromatiques"),
    WILD("Wild", "Sauvages"),
    PERENNIALHERBACEOUS("Perennial Herbaceous","Herbacées Vivaces"),
    PERENNIAL("Perennial", "Vivaces"),
    HERBACEOUS("Herbaceous", "Herbacées"),
    TREE("Tree", "Arbre"),
    BIENNIAL("Biennial", "Bisanuelle"),
    FRUIT("Fruit", "Fruit"),
    BIENNIALHERBACEOUS("Biennial Herbaceous", "Herbacées Bisanuelles"),
    MATERIAL("Material", "Matériau"),
    SHRUB("Shrub", "Arbrisseau"),
    CHILLIPEPPER("Chilli Pepper", "Piment");

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

    public static HerbType findEn(String type){
        HerbType ret = null;
        for(HerbType t : values()){
            if( t.getEn().equals(type)){
                ret = t;
            }
        }
        return ret;
    }

    public static HerbType findFr(String type){
        HerbType ret = null;
        for(HerbType t : values()){
            if( t.getFr().equals(type)){
                ret = t;
            }
        }
        return ret;
    }
}
