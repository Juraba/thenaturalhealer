package com.marmot.intrepid.naturalhealer.service;

import com.marmot.intrepid.naturalhealer.model.*;

import java.util.ArrayList;

public class GameService {

    // ========== MECANIQUE DU SINGLETON ==========

    private static final GameService instance = new GameService();

    public static GameService getInstance() {
        return instance;
    }

    private GameService() {}

    // ========== ATTRIBUTS DE CLASSE DE MODEL ==========

    /*
    private ArrayList<Villager> villagers = Villager.loadVillagers();
    private ArrayList<Item> items = Item.loadItems();

    private Player player = Player.loadPlayer();
    private Grimoire grimoire = Grimoire.loadGrimoire(items);
    private Shop shop = Shop.loadShop(items);
    */

    private ArrayList<Villager> villagers;
    private ArrayList<Item> items;
    private Player player;
    private Grimoire grimoire;
    private Shop shop;

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

    public void setGrimoire(ArrayList<Item> items) {
        this.grimoire = Grimoire.loadGrimoire(items);
    }

    public Shop getShop() {return shop;}

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setShop(ArrayList<Item> items) {
        this.shop = Shop.loadShop(items);
    }
}
