package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum OtherIngredients {
    HONEY("Honey", "Miel"),
    MILK("Milk", "Lait"),
    RHUM("Rhum", "Rhum"),
    LEMON("Lemon", "Citron");

    private String en = "";
    private String fr = "";

    OtherIngredients(String en, String fr){
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
