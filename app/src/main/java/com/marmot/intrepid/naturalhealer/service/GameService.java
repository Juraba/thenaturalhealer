package com.marmot.intrepid.naturalhealer.service;

import android.graphics.Bitmap;

import com.marmot.intrepid.naturalhealer.model.*;
import com.marmot.intrepid.naturalhealer.model.enumerations.QuestType;

import java.util.ArrayList;

public class GameService {

    // ========== MECANIQUE DU SINGLETON ==========

    private static final GameService instance = new GameService();

    public static GameService getInstance() {
        return instance;
    }

    private GameService() {}

    // ========== ATTRIBUTS DE CLASSE DE MODEL ==========

    //private Player player = Player.loadPlayer();
    private Bitmap pic = null;
    private Grimoire grimoire = null;
    private Shop shop = null;
    private ArrayList<Villager> villagers = new ArrayList<Villager>();

    /*
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    */

    /*
    public Bitmap getPic() {
        return pic;
    }
    */

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    /*
    public Grimoire getGrimoire() {
        return grimoire;
    }
    */

    public void setGrimoire(Grimoire grimoire) {
        this.grimoire = grimoire;
    }

    /*
    public Shop getShop() {
        return shop;
    }
    */

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }

    public void setVillagers(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }

    public void createVillagers() {

        //CREATION DES VILLAGEOIS
        Villager v1 = new Villager("Mme.SEGUIN");
        Villager v2 = new Villager("M.LE MAIRE");
        Villager v3 = new Villager("Mme.ROSSIGNOL");
        Villager v4 = new Villager("M.BROSSARD");
        Villager v5 = new Villager("M.RABAULT");

        //CREATION DE QUETES
        //String description, int[] requirements, float[] goals, int rewardMoney, int rewardXp, boolean cancelable, String type
        int[] intQ1 = {1, 5, 6};
        float[] floatQ1 = {1, 5, 6};
        Quest q1 = new Quest("Douleurs musculaires", "Madame SEGUIN a des douleurs musculaires dues à sa vieillesse, trouvez de quoi la soulager !", intQ1, floatQ1, 500, 10, true, QuestType.MAIN);
        int[] intQ2 = {1, 5, 6};
        float[] floatQ2 = {1, 5, 6};
        Quest q2 = new Quest("Stress intense", "Monsieur LE MAIRE est très stressé à cause de son travail, aidez-le à retrouver sa sérénité", intQ1, floatQ1, 500, 10, true, QuestType.DAILY);
        int[] intQ3 = {1, 5, 6};
        float[] floatQ3 = {1, 5, 6};
        Quest q3 = new Quest("Sommeil fuyard", "Monsieur LE MAIRE a du mal à trouver le sommeil ces derniers temps, auriez-vous de quoi l'apaiser ?", intQ1, floatQ1,500,10,true, QuestType.MAIN);
        int[] intQ4 = {1, 5, 6};
        float[] floatQ4 = {1, 5, 6};
        Quest q4 = new Quest("Mal de tête", "Madame ROSSIGNOL subit un affreux mal de tête au travail depuis quelques temps, essayez de stopper cet enfer !", intQ1, floatQ1, 500, 10, true, QuestType.MAIN);
        int[] intQ5 = {1, 5, 6};
        float[] floatQ5 = {1, 5, 6};
        Quest q5 = new Quest("Problème de poids", "Monsieur BROSSARD a pris quelques kilos après les fêtes, concotez de quoi stimuler sa perte de poids !", intQ1, floatQ1, 500, 10, true, QuestType.EVENT);
        int[] intQ6 = {1, 5, 6};
        float[] floatQ6 = {1, 5, 6};
        Quest q6 = new Quest("Rage de dents", "Monsieur RABAULT a une rage de dents depuis maintenant deux jours, aidez-le à supprimer sa douleur", intQ1, floatQ1, 500, 10, true, QuestType.MAIN);

        //AJOUT DES LISTES AUX VILLAGEOIS
        v1.addQuest(q1);
        v2.addQuest(q2);
        v2.addQuest(q3);
        v3.addQuest(q4);
        v4.addQuest(q5);
        v5.addQuest(q6);

        //AJOUT DES VILLAGEOIS A LA LISTE
        villagers.add(v1);
        villagers.add(v2);
        villagers.add(v3);
        villagers.add(v4);
        villagers.add(v5);

    }
}
