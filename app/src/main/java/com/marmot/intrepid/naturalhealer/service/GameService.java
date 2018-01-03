package com.marmot.intrepid.naturalhealer.service;

import android.graphics.Bitmap;

import com.marmot.intrepid.naturalhealer.control.MainActivity;
import com.marmot.intrepid.naturalhealer.data.tables.HerbDAO;
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

    private ArrayList<Villager> villagers = Villager.loadVillagers();
    private ArrayList<Item> items = Item.loadItems();

    private Player player = Player.loadPlayer();
    private Grimoire grimoire = Grimoire.loadGrimoire(items);
    private Shop shop = Shop.loadShop(items);

    //private HerbDAO herbs = new HerbDAO();

    public void fillInventory() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getClass() != OtherIngredients.class) {
                int randomNum = 1 + (int)(Math.random() * 25);
                player.addItems(items.get(i), randomNum);
            }
        }
    }

    public ArrayList<Villager> getVillagers() {
        return villagers;
    }

    public void setVillagers(ArrayList<Villager> villagers) {
        this.villagers = villagers;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Grimoire getGrimoire() {return grimoire;}

    public void setGrimoire(Grimoire grimoire) {
        this.grimoire = grimoire;
    }

    public Shop getShop() {return shop;}

    public void setShop(Shop shop) {
        this.shop = shop;
    }


}
