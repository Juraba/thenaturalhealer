package com.marmot.intrepid.naturalhealer.model.enumerations;

/**
 * Created by PC-Justine on 19/12/2017.
 */

public enum Symptoms {
    TIRED("Tired", "Fatigué"),
    NERVOUS("Nervous", "Nerveux"),
    STRESSED("Stressed", "Stressé"),
    TENSED("Tensed", "Tendu"),
    MUSCLEPAIN("Muscle Pain", "Douleur musculaire"),
    HEADACHE("Headache", "Mal de tête"),
    STOMACHPAIN("Stomach pain", "Douleur à l'estomac"),
    DEPRESSION("Depression", "Dépression"),
    MENSTRUALPAIN("Menstrual Pain", "Douleurs menstruelles"),
    RUNNYNOSE("Runny nose", "Nez encombré"),
    COUGH("Cough", "Toux"),
    WATERYEYES("Watery Eyes", "Yeux larmoyants"),
    SORETHROAT("Sore throat", "Mal de gorge"),
    WEAK("Weak", "Faible"),
    NONE("None", "Aucun");

    private String en = "";
    private String fr = "";

    Symptoms(String en, String fr){
        this.en = en;
        this.fr = fr;
    }

    public String getEn() {
        return this.en;
    }

    public String getFr() {
        return this.fr;
    }

    public static Symptoms findEn(String symptoms){
        Symptoms ret = null;
        for(Symptoms s : values()){
            if( s.getEn().equals(symptoms)){
                ret = s;
            }
        }
        return ret;
    }

    public static Symptoms findFr(String symptoms){
        Symptoms ret = null;
        for(Symptoms s : values()){
            if( s.getFr().equals(symptoms)){
                ret = s;
            }
        }
        return ret;
    }
}
