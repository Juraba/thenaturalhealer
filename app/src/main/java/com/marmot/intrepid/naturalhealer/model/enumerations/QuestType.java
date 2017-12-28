package com.marmot.intrepid.naturalhealer.model.enumerations;

public enum QuestType {
    MAIN("Main", "Principale"),
    DAILY("Daily", "Journalière"),
    EVENT("Event", "Evènementielle");

    private String en = "";
    private String fr = "";

    QuestType(String en, String fr){
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
