package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.marmot.intrepid.naturalhealer.model.Player;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Player.class, parentColumns = "nickname", childColumns = "player_name"),
                @ForeignKey(entity = Item.class, parentColumns = "name", childColumns = "item_name")
        }
)
public class Inventory {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "player_name")
    private String playerName;
    @ColumnInfo(name = "item_name")
    private String itemName;
    @ColumnInfo(name = "item_type")
    private String itemType;
    @ColumnInfo(name = "quantity")
    private int quantity;

    public Inventory(int id, String playerName, String itemName, String itemType, int quantity) {
        this.id = id;
        this.playerName = playerName;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
