package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum RankEnum {
    RECRUIT("Recruit", "Recrue"),
    JUNIOR("Junior", "Débutant"),
    APPRENTICE("Apprentice", "Apprenti"),
    VETERAN("Veteran", "Vétéran"),
    MASTER("Master", "Maître"),
    GODLIKE("Godlike", "Divin");

    private String en = "";
    private String fr = "";

    RankEnum(String en, String fr){
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
