package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum RecipeDifficulty {
    EASY("Easy", "Facile"),
    MEDIUM("Medium", "Moyen"),
    HARD("Hard", "Difficile"),
    EXPERT("Expert", "Expert");

    private String en = "";
    private String fr = "";

    RecipeDifficulty(String en, String fr){
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
