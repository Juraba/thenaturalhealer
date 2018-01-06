package com.marmot.intrepid.naturalhealer.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.marmot.intrepid.naturalhealer.model.Quest;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Quest.class, parentColumns = "name", childColumns = "quest_name"),
                @ForeignKey(entity = Item.class, parentColumns = "name", childColumns = "item_name")
        }
)
public class Requirements {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "quest_name")
    private String questName;
    @ColumnInfo(name = "item_name")
    private String itemName;
    @ColumnInfo(name = "quantity")
    private int quantity;

    public Requirements(int id, String questName, String itemName, int quantity) {
        this.id = id;
        this.questName = questName;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
